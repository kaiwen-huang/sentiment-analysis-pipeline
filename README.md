# HW3 — Microservice Orchestration on the Cloud (GKE)

This repository contains my deployment of the Sentiment Analyzer microservice application to Google Kubernetes Engine (GKE).

Services:

* `sa-frontend`: Nginx serving the React app
* `sa-webapp`: Spring Boot web app
* `sa-logic`: Python service for sentiment analysis

End-to-end flow: browser → webapp → logic → webapp → browser.

# Videos
- Cloud demo: `/demo.mp4`
- Code/config walkthrough: `/code.mp4`

# Images
Great—those are exactly the public-style URLs Docker Hub uses. I wasn’t able to load the repo pages from this environment (Docker Hub blocks headless access), so I won’t claim they’re public from here, but per the homework you just need to **list** the image URLs and make sure they’re public on your side. The tags you used come straight from your build/update scripts. I’ve formatted a README block you can paste in.

# Docker Hub Images (public)

I list all Docker Hub image URLs:

* **Logic (Python)**
  * Repository: [https://hub.docker.com/r/kaiwenhuang/sa-logic](https://hub.docker.com/r/kaiwenhuang/sa-logic)

* **WebApp (Spring Boot)**
  * Repository: [https://hub.docker.com/r/kaiwenhuang/sa-webapp](https://hub.docker.com/r/kaiwenhuang/sa-webapp)

* **Frontend (Nginx/React)**
  * Repository: [https://hub.docker.com/r/kaiwenhuang/sa-frontend](https://hub.docker.com/r/kaiwenhuang/sa-frontend)



# Steps

Got it. Here’s a **Steps** section written in first person, in English, following exactly the order of your scripts. I’m describing what each script does, what I set or check, and what outcome I expect—**no shell commands included**.

---

## Steps (run in this exact order)

> I run these from the repo root. Before starting, I make sure Docker Desktop is running and I’m signed in to Docker Hub.

### 00_mac_setup.sh — set up my Mac tooling

I install Docker Desktop, Google Cloud SDK, `kubectl`, and `gnu-sed`. I also install/enable the GKE auth plugin and set the environment variable so `kubectl` uses it.

**What I expect after it finishes:** Docker Desktop is running; `gcloud` and `kubectl` are available on my PATH.

---

### 01_gke_setup.sh — set my GCP project and create a small zonal GKE cluster

I set my `PROJECT_ID`, region/zone/cluster variables and log in to GCP. I enable Compute and Kubernetes Engine APIs, create a **zonal** GKE cluster sized to avoid quota issues (1 node, e2-medium, pd-standard 30GB), fetch credentials, and create the `sa` namespace.

**What I expect after it finishes:** my current context points to the new cluster; I can see nodes; the `sa` namespace exists.

---

### 02_build_push.sh — build and push my three Docker images (linux/amd64)

I force builds for `linux/amd64` (important on Apple Silicon) and set my Docker Hub username. Then I build and push **sa-logic:v1**, **sa-webapp:v1**, and **sa-frontend:v1** from their respective directories.

**What I expect after it finishes:** all three images exist in my Docker Hub account, publicly accessible.

---

### 03_deploy.sh — apply my Kubernetes manifests to GKE

I substitute my Docker Hub username into the YAMLs, apply the manifests for logic, webapp, frontend (plus the Nginx config), and watch Services so I can see when `sa-frontend` gets an external IP.

**What I expect after it finishes:** Deployments and Services exist in namespace `sa`. Eventually `sa-frontend` shows an External IP I can open in the browser.

---

### 04_build_npm.sh — my Dockerfile expects a prebuilt React

If my frontend Dockerfile is the simple “copy the built static files into Nginx” variant, I compile the React app to produce the `build/` directory and confirm it exists. If I’m using a multi-stage Dockerfile, I usually don’t need this script.

**What I expect after it finishes:** a `sa-frontend/build/` folder with production assets (only needed for the non–multi-stage Dockerfile).

---

### 05_update_frontend.sh — rebuild/push frontend and roll the deployment

I run this because I change React code from `localhost` to `/api/sentiment`. I rebuild **sa-frontend** with a new tag, push it, update the frontend Deployment’s image in the `sa` namespace, and wait for the rollout to complete. 

**What I expect after it finishes:** the frontend Pods are replaced with the new image, and the site serves my latest build.

---

### 06_update_backend_logic.sh — rebuild/push webapp and logic (v2) and roll

I rebuild **sa-webapp** and **sa-logic** for `linux/amd64` with updated tags, push them, update both Deployments, and wait for the rollouts to finish. I use this after changing backend code or Dockerfiles.

**What I expect after it finishes:** both services are running the new images, and their endpoints appear on the corresponding Services.

---

### 07_update_backend_logic.sh — rebuild/push (v3) and ensure the webapp targets the in-cluster logic URL

I rebuild **sa-webapp** and **sa-logic** again (new tags), push them, **set** the webapp’s environment variable `SA_LOGIC_API_URL` to `http://sa-logic:5000`, update the webapp image, and wait for the rollout. This guarantees the webapp calls the logic service by Service DNS, not `localhost`.

**What I expect after it finishes:** webapp logs show requests going to `http://sa-logic:5000/...`, and end-to-end requests from the browser succeed.

---

### After all scripts

I open the external URL from the `sa-frontend` Service and test the app by submitting a sentence. For grading, I also include the two required videos (cloud demo and code/config walkthrough) and list my three public Docker Hub image URLs in the README, per the assignment’s submission guidelines. 
