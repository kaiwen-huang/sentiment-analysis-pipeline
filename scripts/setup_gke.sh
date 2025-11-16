# Install prereqs (Homebrew)
brew install --cask docker google-cloud-sdk
brew install kubectl gnu-sed

# Open Docker Desktop app (whale icon) and wait until "Docker is running"
# Optional (fix auth on newer GKE):
gcloud components install gke-gcloud-auth-plugin || true
export USE_GKE_GCLOUD_AUTH_PLUGIN=True


# Set your variables
export PROJECT_ID="capable-matrix-472522-d2"
export REGION="us-central1"
export CLUSTER="sa-cluster"
export ZONE="us-central1-a"

# Login to Docker Hub
# docker login

# Login to GCP
gcloud auth login
gcloud config set project $PROJECT_ID
gcloud config set compute/region $REGION

# Enable required API and create a GKE cluster
gcloud services enable compute.googleapis.com container.googleapis.com
gcloud container clusters create $CLUSTER --zone $ZONE --num-nodes=1 --machine-type=e2-medium --disk-type=pd-standard --disk-size=30
gcloud container clusters get-credentials $CLUSTER --zone $ZONE

# Create a separate namespace for this homework (optional but cleaner)
kubectl get nodes
kubectl create namespace sa
