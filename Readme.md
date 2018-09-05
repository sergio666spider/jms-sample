while true; do wget -q -O- http://192.168.99.100:31213/computeSquare?quantity=100000; done

kubectl expose deployment sample-deployment --type=LoadBalancer

minikube service sample-deployment

----------------------------------
