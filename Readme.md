# HPA Examples
## Prerequisites
First of all, install [iHeartMedia custom metrics api-server](https://github.com/ihm-software/ihm-metrics-api-server) in Minikube, so that you have the customized metric configured in the local cluster. If you did not do this yet, please read the instructions in the repository and follow the step-by-step.
Next, you need to install and setup a local RabbitMQ server. This can be easily done by running the following Docker command:
```sh
docker run -d --hostname host-rabbitmq --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```
Last, open your favorite browser window and navigate to [http://localhost:15672/](http://localhost:15672/). If you successfully installed RabbitMq using the Docker image, you will be prompted with a user id and password login page to access RabbitMq management console. Type *guest*/*guest*, log to the RabbitMQ management dashborad and go to the *Admin* tab. Then, add a new user id named *test*, password *test* and *Admin* profile.

## Horizontal POD Autoscaling based on queue "*messages*" attribute
1. Start Minikube using the following command:
```sh
minikube start --memory 4096 --extra-config=controller-manager.horizontal-pod-autoscaler-upscale-delay=10s  --extra-config=controller-manager.horizontal-pod-autoscaler-downscale-delay=10s --extra-config=controller-manager.horizontal-pod-autoscaler-sync-period=10s
```

2. Set Minikube to use its Docker daemon, instead of local Docker repository:
```sh
eval $(minikube docker-env)
```

3. Edit *JmsSampleApplication* class and change the IP address to use your local IP

4. Now build the application and deploy its image to Docker:
```sh
./gradlew build docker
```
If you run the command *docker images*, you should see the project's image successfully pushed to Minikube Docker's repository:
|REPOSITORY|TAG|IMAGE ID|CREATED|SIZE|
| ------ | ------ | ------ | ------ |------ |
|com.iheart/jms-sample|latest|fee6819d7951|About a minute ago|   121MB|

5. Next, deploy the project's image to k8s by running the command:
```sh
kubectl apply -f deploy/deployment/sample-deployment.yaml
```
If everything ran well, you should see a *sample-deployment* POD returned by the command *kubectl get pods*. It should be in *RUNNING* status:
|NAME|READY|STATUS|RESTARTS|AGE|
| ------ | ------ | ------ | ------ |------ |
|sample-deployment-77ddb4df7d-d9gpp|1/1|Running|0|23s|

6. Now it is the great time to create the HPA based on *queue_messages* metric:
```sh
kubectl apply -f deploy/hpa/hpa-queue-sample.yaml
```
Running the command *kubectl get hpa* a few seconds later will probably return the metric with *unknown* TARGET value:
|NAME|REFERENCE|TARGETS|MINPODS|MAXPODS|REPLICAS|AGE|
| ------ | ------ | ------ | ------ |------ | ------ | ------ |
|queue-hpa|Deployment/sample-deployment|<unknown>/250 (avg)|1|5|0|10s|
This is expected because Kubernetes delays a few seconds to read and update the metric value. Wait one minute or so and the value should be updated to zero:
|NAME|REFERENCE|TARGETS|MINPODS|MAXPODS|REPLICAS|AGE|
| ------ | ------ | ------ | ------ |------ | ------ | ------ |
|queue-hpa|Deployment/sample-deployment|0/250 (avg)|1|5|0|10s|

7. Let's produce a bunch of messages in a test queue to simulate a heavy load and, hence, see the HPA working as expected. Type these commands to expose the application as a load-balancer service:
```sh
kubectl expose deployment sample-deployment --type=LoadBalancer

minikube service sample-deployment
```
The second command will open a browser window with a URL. Append */sendMessage?quantity=1000* to it and hit *Enter*: this endpoint simulates 1000 messages being posted to the *test* queue.
Go back to the terminal and hit *kubectl get hpa* until the metric value is updated with the current quantity of messages in the queue:


8. After 1 minute or so, run *kubectl get all* and see that the number of PODs were dinamically increased based on the metric. Run this command over and over again to monitor the application and check the autoscaling up and down based on the processing of the messages in the queue.
