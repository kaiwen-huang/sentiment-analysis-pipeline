# Sentiment Analysis Pipeline

Real-time sentiment analysis pipeline built with Kafka, Docker, and Kubernetes.

## Project overview

This repository demonstrates an event-driven, scalable sentiment analysis platform. The system is implemented as multiple microservices: a React-based frontend, a Python-based sentiment analysis service, and a Java backend web application. Services communicate via Apache Kafka for message streaming and decoupling. Each service is containerized with Docker and deployment manifests are provided for Kubernetes.

Purpose: to showcase microservice architecture, real-time streaming with Kafka, containerization, Kubernetes deployments, and simple CI/CD automation.

## Architecture

- Frontend (React): collects sentences from users, displays analysis results, and provides a simple UI for interaction.
- Backend API (`sa-webapp` - Java Spring Boot): exposes HTTP endpoints for the frontend, integrates with Kafka using a request/reply pattern.
- Logic service (`sa-logic` - Python): consumes requests from Kafka, runs sentiment analysis (simple NLP), and replies with results.
- Apache Kafka: message broker used for request/response and event streaming.
- Kubernetes manifests (`infra/`, `resource-manifests/`): deployment, service, and load-balancer definitions for cluster deployment.

High-level flow:
1. User submits a sentence in the frontend → POST to `sa-webapp`.
2. `sa-webapp` publishes a request message to a Kafka topic (request topic).
3. `sa-logic` consumes the request, performs sentiment analysis, and writes the result to a reply/result topic.
4. `sa-webapp` (or frontend) reads the reply and returns the analysis to the user.

## Components

- `sa-frontend/`: React application (source and pre-built `build/`).
- `sa-logic/`: Python sentiment analysis service and dependencies (`sa/requirements.txt`).
- `sa-webapp/`: Java Spring Boot application (`pom.xml`, `src/main/java`) with Kafka integration.
- `infra/` & `resource-manifests/`: Kubernetes manifests for Kafka and each service.
- `scripts/`: helper scripts for build, push, and deployment (e.g., `scripts/hw3/02_build_push.sh`, `03_deploy.sh`).

## Tech stack

- Frontend: React (Node.js, npm)
- Backend: Java (Spring Boot, Maven)
- Logic: Python (simple NLP)
- Messaging: Apache Kafka
- Containerization: Docker
- Orchestration: Kubernetes
- CI/CD: shell scripts included; easily adaptable to GitHub Actions/GitLab CI

## Quick start (development & local testing)

Prerequisites:
- git
- Docker
- Java 11+ and Maven
- Python 3.8+
- Node.js and npm
- A running Kafka broker (local or in Kubernetes)

Clone the repo:

```bash
git clone https://github.com/<your-user>/hw-3-microservice-orchestraction-kaiwenhu-cmu.git
cd infra-mastery
```

Run frontend (development mode):

```bash
cd sa-frontend
npm install
npm start
```

Run sentiment logic locally:

```bash
cd sa-logic/sa
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python sentiment_analysis.py
```

Run the Java backend:

```bash
cd sa-webapp
./mvnw spring-boot:run
```

Note: If you do not have a local Kafka instance, you can run a single-node Kafka container for development or adapt the services to communicate over HTTP for local testing.

## Docker build & push

Each service contains a Dockerfile. Example build and push commands:

```bash
# Build and push all images (example script)
sh scripts/hw3/02_build_push.sh

# Or build individually
cd sa-frontend && docker build -t <registry>/sa-frontend:latest .
cd ../sa-logic && docker build -t <registry>/sa-logic:latest .
cd ../sa-webapp && docker build -t <registry>/sa-webapp:latest .
```

Replace `<registry>` with your container registry (Docker Hub, GCR, ECR, etc.). The scripts demonstrate tagging and pushing images.

## Kubernetes deployment

Apply manifests from `infra/` and `resource-manifests/` to deploy Kafka and the services:

```bash
# Ensure kubectl is configured to the desired cluster
kubectl apply -f infra/kafka-stable.yaml
kubectl apply -f infra/sa-webapp.yaml
kubectl apply -f infra/sa-logic.yaml
kubectl apply -f infra/sa-frontend.yaml
```

Check resources and logs:

```bash
kubectl get pods,svc -n default
kubectl logs deployment/sa-webapp
```

For local cluster testing, consider using Minikube or kind and push images into the local cluster registry.

## Testing & verification

- Frontend: open the app in a browser, submit a sentence, and observe the sentiment result.
- Backend: use curl or Postman to POST to the API and verify the Kafka request/reply flow.

Example curl:

```bash
curl -X POST "http://<sa-webapp-host>:<port>/api/sentiment" \
	-H "Content-Type: application/json" \
	-d '{"sentence":"I love this service"}'
```
