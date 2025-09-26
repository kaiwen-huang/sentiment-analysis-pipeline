export DOCKER_DEFAULT_PLATFORM=linux/amd64

docker build -t "${DOCKERHUB_USER}/sa-webapp:v2"  ./sa-webapp
docker push "${DOCKERHUB_USER}/sa-webapp:v2"

docker build -t "${DOCKERHUB_USER}/sa-logic:v2"   ./sa-logic
docker push "${DOCKERHUB_USER}/sa-logic:v2"

kubectl -n sa set image deploy/sa-webapp webapp="${DOCKERHUB_USER}/sa-webapp:v2"
kubectl -n sa set image deploy/sa-logic  logic="${DOCKERHUB_USER}/sa-logic:v2"
kubectl -n sa rollout status deploy/sa-webapp
kubectl -n sa rollout status deploy/sa-logic
