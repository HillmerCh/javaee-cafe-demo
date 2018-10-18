# Deploy on Oracle Database and Google Cloud

This demo shows using Java EE thin wars with Docker repositories. It connects with a Oracle Database from Oracle Cloud Service. 

It uses `Payara Server` image, that is available from the online Docker Hub repository, to creates a Docker image that is deployed on Google Cloud.

The following is how you run the demo.


## Configure and Create a Application Docker Image

* Activate a Cloud Shell

* Clone the project 

	```bash
	git clone https://github.com/HillmerCh/javaee-cafe-demo.git
	```
	
* Edit the file src/main/webapp/WEB-INF/glassfish-resources.xml with the Database connection parameters


	```
	<property name="URL" value="jdbc:oracle:thin:@//<MY_ORACLE_CLOUD_CONNECT_STRING>"/>
	<property name="User" value="<MY_DABATASE_USER>"/>
	<property name="Password" value="<MY_DATABASE_USERS_PASSWORD>"/>
	```
	
* 	Generate the .war file with maven  

	```bash
	mvn clean package
	```

* 	Copy the .war file with maven 

	```bash
	cp  ../javaee-app/target/javaee-cafe-demo.war .
	```

* Build a Docker image tagged `javaee-cafe-demo` issuing the command:

	```bash
	docker build -t javaee-cafe-demo .
	```

*  Run the newly built image with the command:

	```
	docker run -it --rm  --name javaee-demo -p 8080:8080 -p 4848:4848 javaee-cafe-demo
	```
	
* Wait for Payara Server to start and the application to deploy sucessfully (to stop the application and Payara, simply press Control-C).
 

* Once the application starts, you can test the REST service at the URL: [http://localhost:8080/javaee-cafe/rest/coffees](http://localhost:8080/javaee-cafe/rest/coffees) or via the JSF client at [http://localhost:9080/javaee-cafe/index.xhtml](http://localhost:9080/javaee-cafe/index.xhtml).


Open Web Preview on Port 8080 to test