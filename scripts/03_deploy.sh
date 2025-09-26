# Replace placeholders in YAML quickly (optional helper)
sed -i.bak "s|DOCKERHUB_USER|$DOCKERHUB_USER|g" k8s/*.yaml

# Apply manifests
kubectl apply -f k8s/sa-logic.yaml
kubectl apply -f k8s/sa-webapp.yaml
kubectl apply -f k8s/sa-frontend-nginx-config.yaml
kubectl apply -f k8s/sa-frontend.yaml

# Watch for the external IP of the frontend Service
kubectl get svc -n sa -w
