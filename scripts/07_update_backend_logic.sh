export DOCKER_DEFAULT_PLATFORM=linux/amd64

docker build -t "${DOCKERHUB_USER}/sa-webapp:v3"  ./sa-webapp
docker push "${DOCKERHUB_USER}/sa-webapp:v3"

docker build -t "${DOCKERHUB_USER}/sa-logic:v3"   ./sa-logic
docker push "${DOCKERHUB_USER}/sa-logic:v3"

kubectl -n sa set env deploy/sa-webapp SA_LOGIC_API_URL="http://sa-logic:5000"
kubectl -n sa set image deploy/sa-webapp webapp="${DOCKERHUB_USER}/sa-webapp:v3"
kubectl -n sa rollout status deploy/sa-webapp
