# Install prereqs (Homebrew)
brew install --cask docker google-cloud-sdk
brew install kubectl gnu-sed

# Open Docker Desktop app (whale icon) and wait until "Docker is running"
# Optional (fix auth on newer GKE):
gcloud components install gke-gcloud-auth-plugin || true
export USE_GKE_GCLOUD_AUTH_PLUGIN=True
