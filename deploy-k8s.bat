@echo off
echo Deploying RevWorkForce to Kubernetes (Minikube)...

:: Create Namespace
kubectl apply -f - <<EOF
apiVersion: v1
kind: Namespace
metadata:
  name: revworkforce
EOF

:: Apply Config and Secrets
kubectl apply -f devops/kubernetes/configmap/app-config.yaml
kubectl apply -f devops/kubernetes/secrets/app-secrets.yaml

:: Apply Persistence
kubectl apply -f devops/kubernetes/deployment/mysql-pvc.yaml
kubectl apply -f devops/kubernetes/deployment/mysql.yaml

:: Apply Infrastructure
kubectl apply -f devops/kubernetes/deployment/eureka-server.yaml
kubectl apply -f devops/kubernetes/deployment/config-server.yaml
kubectl apply -f devops/kubernetes/deployment/api-gateway.yaml

:: Apply Services
kubectl apply -f devops/kubernetes/deployment/user-service.yaml
kubectl apply -f devops/kubernetes/deployment/employee-management-service.yaml
kubectl apply -f devops/kubernetes/deployment/leave-service.yaml
kubectl apply -f devops/kubernetes/deployment/performance-service.yaml
kubectl apply -f devops/kubernetes/deployment/notification-service.yaml
kubectl apply -f devops/kubernetes/deployment/reporting-service.yaml

:: Apply UI
kubectl apply -f devops/kubernetes/deployment/revworkforce-ui.yaml

:: Apply HPA and Ingress
kubectl apply -f devops/kubernetes/hpa/app-hpa.yaml
kubectl apply -f devops/kubernetes/ingress/app-ingress.yaml

echo Deployment complete. Run 'minikube tunnel' if on Windows/Mac for LoadBalancer and Ingress access.
