minikube start --driver=hyperv
minikube addons enable ingress
kubectl create deployment home --image=rubinjo/home-service:1.0.6
kubectl expose deployment home --type=NodePort --port=8083
kubectl create deployment movie --image=rubinjo/movie-service:1.0.7
kubectl expose deployment movie --type=NodePort --port=8081
kubectl create deployment food --image=rubinjo/food-service:1.0.4
kubectl expose deployment food --type=NodePort --port=8084
kubectl create deployment auth --image=rubinjo/auth-service:1.0.1
kubectl expose deployment auth --type=NodePort --port=8085
kubectl create deployment reserve --image=rubinjo/reserve-service:1.0.0
kubectl expose deployment reserve --type=NodePort --port=8082
sleep 2
kubectl apply -f ingress.yml