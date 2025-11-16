# Replace placeholders in YAML quickly (optional helper)
sed -i.bak "s|DOCKERHUB_USER|$DOCKERHUB_USER|g" infra/*.yaml

# Apply manifests
kubectl apply -f infra/sa-logic.yaml
kubectl apply -f infra/sa-webapp.yaml
kubectl apply -f infra/sa-frontend-nginx-config.yaml
kubectl apply -f infra/sa-frontend.yaml

# Watch for the external IP of the frontend Service
kubectl get svc -n sa -w
