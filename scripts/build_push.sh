# Build & push the Python SA-Logic image
export DOCKER_DEFAULT_PLATFORM=linux/amd64
export DOCKERHUB_USER="kaiwenhuang"

# Adjust the Dockerfile path to where the Logic service is in your repo.
docker build -t $DOCKERHUB_USER/sa-logic:v1 ./sa-logic
docker push $DOCKERHUB_USER/sa-logic:v1

# Build & push the Java SA-WebApp image
docker build -t $DOCKERHUB_USER/sa-webapp:v1 ./sa-webapp
docker push $DOCKERHUB_USER/sa-webapp:v1

# Build & push the Nginx/React SA-Frontend image
docker build -t $DOCKERHUB_USER/sa-frontend:v1 ./sa-frontend
docker push $DOCKERHUB_USER/sa-frontend:v1
