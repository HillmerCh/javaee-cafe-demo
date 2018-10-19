# Deploy on Oracle Database and Google Cloud

This demo shows using Java EE thin wars with Docker repositories. It connects with a Oracle Database from Oracle Cloud Service. 

It uses `Payara Server` image, that is available from the online Docker Hub repository, to creates a Docker image that is deployed on Google Cloud.

The following is how you run the demo.


## Configure and Create a Application Docker Image

* Activate a Cloud Shell
	```
	gcloud config set compute/zone us-central1-b
	```

* Clone the project 
	```bash
	git clone https://github.com/HillmerCh/javaee-cafe-demo.git
	```
	
* Edit the file javaee-app/src/main/webapp/WEB-INF/glassfish-resources.xml with the Database connection parameters
	```
	<property name="URL" value="jdbc:oracle:thin:@//<MY_ORACLE_CLOUD_CONNECT_STRING>"/>
	<property name="User" value="<MY_DABATASE_USER>"/>
	<property name="Password" value="<MY_DATABASE_USERS_PASSWORD>"/>
	```
	
* 	Generate the .war file with maven  
	```bash
	mvn clean package --file ../javaee-app/pom.xml 
	```

* 	Copy the .war file with maven 
	```bash
	cp  ../javaee-app/target/javaee-cafe-demo.war .
	```

* Build a Docker image tagged `gcr.io/myjenkinsjee/javaee-cafe-demo:v1` issuing the command: 
	```bash
	docker build -t gcr.io/myjenkinsjee/javaee-cafe-demo:v1 .
	```

*  Run the newly built image with the command: To test your container image using your local Docker engine, run the following command:
	```
	docker run -it --rm  --name javaee-cafe-demo -p 8080:8080 -p 4848:4848 gcr.io/myjenkinsjee/javaee-cafe-demo:v1
	```
	
* Wait for Payara Server to start and the application to deploy sucessfully (to stop the application and Payara, simply press Control-C).
 
* Open Web Preview on Port 8080 to access the app adding to the URL `javaee-cafe-demo/index.xhtml`


## Upload the container image

* Configure Docker command-line tool to authenticate to Container Registry (It must be run only once):
	```
	gcloud auth configure-docker
	```
* Upload the container image to your Container Registry:
	```
	docker push gcr.io/<MY_PROJECT_ID>/javaee-cafe-demo:v1
	```

* Build the container image of this application and tag it for uploading:
	```
	docker build -t gcr.io/<MY_PROJECT_ID>/javaee-cafe-demo:v1 .
	```

## Create a Kubernetes Cluster

* Use Google Container Engine to create and manage a Kubernetes cluster. Provision the cluster with gcloud:
	```
	gcloud container clusters create javaee-cafe-cluster --num-nodes=1
    ```
	
	Wait for a time the operation is complete

* Download the credentials for the cluster using the gcloud CLI:
	```
	$ gcloud container clusters get-credentials javaee-cafe-cluster
	```
* Confirm that the cluster is running and kubectl is working by listing pods:
	```
	$ kubectl get pods
	```
	
## Deploy the application:

* Run the following command to deploy the application, listening on port 8080:
	```
	kubectl run javaee-cafe-demo --image=gcr.io/<MY_PROJECT_ID>/javaee-cafe-demo:v1 --port 8080
	```

* See the Pod created by the Deployment:
	```
	kubectl get pods
	```
  
* Expose the application to the Internet   
	```   
   kubectl expose deployment javaee-cafe-demo --type=LoadBalancer --port 80 --target-port 8080
	```