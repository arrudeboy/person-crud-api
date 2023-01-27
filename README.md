# person-crud-api

> Spring Boot REST API for Reba Challenge. 

### requirements
In order to run this server you will need the following:

1. __Docker and Docker Compose__:
  
    * If you have installed Docker and Docker Compose, you can check the version by typing the following in a command line:
    
        ```
        docker --version
        docker-compose --version
        ```
2. __Maven__
    * If you have Maven, you can check the version by typing the following in a command line:
        ```
        mvn --version
        ```

### deploy

1. Build the project package by typing the following in a command line:
    ```bash
    $ mvn clean package
    ```

2. Copy the .jar file located at target folder to src/main/docker folder:

    ```bash
    $ cp target/person-crud-api-0.0.1-SNAPSHOT.jar src/main/docker
    ```
    
2. Go to src/main/docker folder and use docker-compose tool to build the services (app and db) by typing:

    ```bash
    $ docker-compose up
    ```
### usage

1. Now open a web browser and go to http://localhost:8080/api/swagger-ui.html

2. Play around with swagger ui by doing requests to the rest api endpoints:

![playground](https://i.imgur.com/8zyMGMy.png)

* __Deployed instance at Amazon AWS__:

    * API: http://ec2-18-224-31-39.us-east-2.compute.amazonaws.com/api
    
    * Swagger UI playground: http://ec2-18-224-31-39.us-east-2.compute.amazonaws.com/api/swagger-ui.html


### performance test

* Run performance tests by typing the following command in a terminal:
     ```bash
     $ mvn clean gatling:test
     ```

* Command output details the test results. Also, check the link above in order to see test results as charts:

![test-results](https://i.imgur.com/NBYj6rI.png)

![test-results-charts](https://i.imgur.com/AqYf6mH.png)

### stack
* Java 11
* Maven
* Spring Boot
* Hibernate/JPA
* PostgresSQL
* Swagger
* Gatling
* Lombok
* Amazon AWS
* Nginx

##### pending items:
* unit-tests with JUnit and Mockito
