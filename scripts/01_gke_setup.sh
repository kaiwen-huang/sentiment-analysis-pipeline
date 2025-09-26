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
