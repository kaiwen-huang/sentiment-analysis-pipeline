export DOCKER_DEFAULT_PLATFORM=linux/amd64

docker build -t "${DOCKERHUB_USER}/sa-frontend:v3" ./sa-frontend
docker push "${DOCKERHUB_USER}/sa-frontend:v3"

kubectl set image deploy/sa-frontend -n sa \
  frontend="${DOCKERHUB_USER}/sa-frontend:v3"
kubectl rollout status deploy/sa-frontend -n sa
