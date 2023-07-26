# README #
This README is a simple technical report about the developed work for the Class Assignment 4 (part 2). <br>

This part of the Class Assignment was about <strong>Containers with Docker</strong>. <br>

The goal of the second part of CA4 was to use Docker to setup a containerized environment to execute our version of the gradle version of the spring basic tutorial application. <br>
This report will cover everything that was done for this work, step by step. It will also cover the used approach with brief explanations and all the used commands.


## CA4 - Part 2 ##
### Using docker-compose to produce 2 services/containers ###
In containerization and microservices, efficiently managing multiple Docker containers is crucial for modern application development. <br>
One powerful tool that simplifies this process is docker-compose. By using docker-compose, we can define and manage multiple services/containers as part of an application, allowing them to work together seamlessly. <br>
The key file in Docker Compose is the docker-compose.yml file. This file is used to define and configure the services, networks, and volumes for your application. <br>
It allows you to specify the containers, their configurations, dependencies, and how they should interact with each other. <br>
The docker-compose.yml file serves as a central configuration file that orchestrates the deployment and management of multiple containers as a single application.<br>

So, to start working on this part of the assignment, you must create a new directory. If you are at CA4, run: <br>

> &#36; mkdir -p Part2/db web <br>

With this command, you are creating the Part2 directory related to this assignment. Inside, there will be a directory that is related to the H2 database and another one that is related to the Tomcat. <br>

Then, you should create a Dockerfile for the H2 database and other for the Tomcat. <br>
Creating separate Dockerfiles for the H2 database and the Tomcat server allows you to define the specific configurations and dependencies for each component individually. <br>
This separation ensures that each container is built and configured correctly for its specific purpose. <br>

So, let's create the Dockerfile for the H2 database. Being inside the CA4/Part2, you should run the commands: <br>

> &#36; cd db <br>
> &#36; touch Dockerfile <br>

Open the Dockerfile and add these instructions to define your Docker image: <br>

```Docker
## DOCKERFILE H2 DB
FROM ubuntu

RUN apt-get update && \
  apt-get install -y openjdk-8-jdk-headless && \
  apt-get install unzip -y && \
  apt-get install wget -y

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app/

RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar

EXPOSE 8082
EXPOSE 9092

CMD java -cp ./h2-1.4.200.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists
```

Here's a breakdown of the Dockerfile: <br>

* <strong>FROM ubuntu</strong> - This line specifies the base image for your Docker image. In this case, it uses the Ubuntu image as the starting point. <br>
* <strong>RUN apt-get update && \ <br>
  apt-get install -y openjdk-8-jdk-headless && \ <br>
  apt-get install unzip -y && \ <br>
  apt-get install wget -y</strong> - These lines update the package repositories within the Ubuntu image and install the necessary dependencies such as OpenJDK 8, unzip, and wget.<br>
* <strong>RUN mkdir -p /usr/src/app</strong> - This command creates a directory named /usr/src/app within the Docker image. <br>
* <strong>WORKDIR /usr/src/app/</strong> - This instruction sets the working directory for subsequent commands to /usr/src/app.<br>
* <strong>RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar </strong> - This command uses wget to download the H2 database JAR file from the specified URL and saves it in the current working directory (/usr/src/app).<br>
* <strong>EXPOSE 8082 <br>
  EXPOSE 9092</strong> - These lines specify the ports that should be exposed by the Docker container. In this case, port 8082 and 9092 are exposed to allow incoming connections. <br>
* <strong>CMD java -cp ./h2-1.4.200.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists</strong> - This instruction defines the command that will be executed when the container starts. It launches the H2 database server by running the java command with the specified classpath (-cp) and server configurations (org.h2.tools.Server). The additional options (-web, -webAllowOthers, -tcp, -tcpAllowOthers, -ifNotExists) enable web and TCP access to the database server and create the database if it doesn't already exist.<br>

Once you have the Dockerfile for the database, let's create the Dockerfile for the Tomcat. Being inside the CA4/Part2, you should run the commands: <br>

> &#36; cd web <br>
> &#36; touch Dockerfile <br>

Open the Dockerfile and add these instructions to define your Docker image: <br>

```Docker
## DOCKERFILE TOMCAT
FROM tomcat:9.0.53-jdk11-temurin-focal

RUN apt-get update && apt-get install -y --no-install-recommends apt-utils

RUN apt-get install -y curl
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash -
RUN apt-get install -y nodejs

RUN apt-get install sudo nano git -y

# Install unzip package
RUN apt-get install -y unzip

RUN apt-get clean && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /tmp/build

WORKDIR /tmp/build/

RUN git clone https://marta_lopes@bitbucket.org/marta_lopes/devops-22-23-maal-1221850.git

WORKDIR /tmp/build/devops-22-23-maal-1221850/CA3/Part2/react-and-spring-data-rest-basic/

# Download and configure Gradle
RUN curl -sL https://services.gradle.org/distributions/gradle-6.9.1-bin.zip -o gradle.zip \
    && unzip gradle.zip \
    && mv gradle-6.9.1 /opt/gradle \
    && rm gradle.zip

# Set Gradle environment variables
ENV GRADLE_HOME=/opt/gradle
ENV PATH=$PATH:$GRADLE_HOME/bin

RUN chmod +x gradlew

RUN ./gradlew clean build && cp build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ && rm -Rf /tmp/build/
EXPOSE 8080

```

Here's a breakdown of the Dockerfile: <br>

* <strong>FROM tomcat:9.0.53-jdk11-temurin-focal</strong> - This line specifies the base image for your Docker image. It uses the Tomcat image with version 9.0.53 and JDK 11 (Temurin) based on Ubuntu Focal as the starting point. <br>
* <strong>RUN apt-get update && apt-get install -y --no-install-recommends apt-utils</strong> - This command updates the package repositories and installs the necessary packages for package management (apt-utils) without installing recommended packages.<br>
* <strong>RUN apt-get install -y curl <br>
  RUN curl -sL https://deb.nodesource.com/setup_14.x | bash - <br>
  RUN apt-get install -y nodejs</strong> - These lines install curl and then use it to download and execute a script from NodeSource to install Node.js version 14.x. It installs Node.js and its dependencies. <br>
* <strong>RUN apt-get install sudo nano git -y</strong> - This command installs sudo, nano, and git packages.<br>
* <strong>RUN apt-get install -y unzip</strong> - This line installs the unzip package.<br>
* <strong>RUN apt-get clean && rm -rf /var/lib/apt/lists/*</strong> - This command cleans the package cache and removes the lists of available packages.<br>
* <strong>RUN mkdir -p /tmp/build <br>
  WORKDIR /tmp/build/</strong> - These lines create a directory /tmp/build and set it as the working directory.<br>
* <strong>RUN git clone https://marta_lopes@bitbucket.org/marta_lopes/devops-22-23-maal-1221850.git</strong> - This command clones a Git repository into the current working directory. It fetches the project from the specified URL.<br>
* <strong>WORKDIR /tmp/build/devops-22-23-maal-1221850/CA3/Part2/react-and-spring-data-rest-basic/</strong> - This line changes the working directory to the specified path within the cloned repository.<br>
* <strong>RUN curl -sL https://services.gradle.org/distributions/gradle-6.9.1-bin.zip -o gradle.zip \ <br>
  && unzip gradle.zip \ <br>
  && mv gradle-6.9.1 /opt/gradle \ <br>
  && rm gradle.zip</strong> - These commands download Gradle distribution, unzip it, move it to /opt/gradle directory, and remove the downloaded ZIP file.<br>
* <strong>ENV GRADLE_HOME=/opt/gradle <br>
  ENV PATH=$PATH:$GRADLE_HOME/bin</strong> - These lines set environment variables for Gradle's home directory and update the PATH variable to include Gradle's bin directory.<br>
* <strong>RUN chmod +x gradlew</strong> - This command makes the gradlew script executable.<br>
* <strong>RUN ./gradlew clean build && cp build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ && rm -Rf /tmp/build/</strong> - This line cleans the project, builds it using gradlew, copies the resulting WAR file to the Tomcat's webapps directory, and finally removes the temporary build directory.<br>
* <strong>EXPOSE 8080</strong> - This instruction specifies that the container will expose port 8080, allowing incoming connections.<br>

Now that we finally have both Dockerfiles, let's create the docker-compose.yml file. <br>
Being inside the CA4/Part2 directory, run the following command: <br>

> &#36; touch docker-compose.yml <br>

Open the docker-compose.yml file and add the following content to define your services: <br>

```yaml
version: '3'
services:
  web:
    build:
      context: ./web
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    networks:
      default:
        ipv4_address: 192.168.56.10
    depends_on:
      - db
  db:
    build:
        context: ./db
        dockerfile: Dockerfile
    ports:
      - "8082:8082"
      - "9092:9092"
    volumes:
      - ./data:/usr/src/data-backup
    networks:
      default:
        ipv4_address: 192.168.56.11
networks:
  default:
    ipam:
      driver: default
      config:
        - subnet: 192.168.56.0/24
```

Here's a breakdown of the docker-compose.yml file: <br>

* <strong>version: '3'</strong> - Specifies the version of the Compose file format being used.<br>
* <strong>services: <br>
  web: <br>
  build: <br>
  context: ./web <br>
  dockerfile: Dockerfile</strong> - Defines a service named web for the Tomcat server. Specifies the build context as ./web and the Dockerfile as Dockerfile. This means that the Docker image for the web service will be built using the instructions in the Dockerfile located in the ./web directory. <br>
* <strong> ports: <br>
      - "8080:8080"</strong> - Maps port 8080 of the host to port 8080 of the container. Allows accessing the Tomcat server running inside the container through localhost:8080 on the host machine.<br>
* <strong>networks: <br>
  default: <br>
  ipv4_address: 192.168.56.10</strong> - Adds the web service to the default network. Assigns a specific IPv4 address (192.168.56.10) to the container within the network.<br>
* <strong>depends_on: <br>
      - db</strong> - pecifies that the web service depends on the db service. This ensures that the H2 database container is started before the Tomcat server container.<br>
* <strong>db: <br>
    build: <br>
        context: ./db <br>
        dockerfile: Dockerfile</strong> - Defines a service named db for the H2 database. Specifies the build context as ./db and the Dockerfile as Dockerfile. This means that the Docker image for the db service will be built using the instructions in the Dockerfile located in the ./db directory.<br>
* <strong> ports: <br>
      - "8082:8082" <br>
      - "9092:9092"</strong> - Maps port 8082 and 9092 of the host to the respective ports of the container. Allows accessing the H2 database server running inside the container through localhost:8082 and localhost:9092 on the host machine.<br>
* <strong>volumes: <br>
        - ./data:/usr/src/data-backup</strong> - Mounts the ./data directory on the host to /usr/src/data-backup inside the container. Allows persisting data from the H2 database outside the container.<br>
* <strong> networks: <br>
      default: <br>
        ipv4_address: 192.168.56.11</strong> - Adds the db service to the default network. Assigns a specific IPv4 address (192.168.56.11) to the container within the network.<br>
* <strong>networks: <br>
  default: <br>
  ipam: <br>
  driver: default <br>
  config: <br>
        - subnet: 192.168.56.0/24</strong> - Defines the default network for the services. Configures the IP address management (IPAM) for the network. Specifies the subnet as 192.168.56.0/24, allowing IP addresses in the range 192.168.56.1 to 192.168.56.254 to be assigned within the network.<br>

The docker-compose.yml file manages/configures the deployment and management of the H2 database and Tomcat server containers. <br>
It defines their configurations, dependencies, port mappings, volume mounts, and network settings. With this file, you can use the docker-compose command to start, stop and manage both services together as a single application. <br>

So, being by the same level as the docker-compose.yml file, let's run the command: <br>

> &#36; docker-compose build<br>

And then: <br>

> &#36; docker-compose up<br>

If you run the command: <br>

> &#36; docker images<br>

This will be its content: <br>

````
REPOSITORY          TAG       IMAGE ID       CREATED        SIZE
part2-web           latest    d9d38b9a9c3e   2 hours ago    1.45GB
part2-db            latest    657651a79fbd   4 hours ago    306MB
````

If you run the command: <br>

> &#36; docker network ls<br>

This will be its content: <br>

````
NETWORK ID     NAME            DRIVER    SCOPE
bdfd4d6b053c   part2_default   bridge    local
````

And if you run the command: <br>

> &#36; docker ps<br>

This will be its content: <br>

````
CONTAINER ID   IMAGE       COMMAND                  CREATED             STATUS             PORTS                                            NAMES
ea2615226623   part2-web   "catalina.sh run"        About an hour ago   Up About an hour   0.0.0.0:8080->8080/tcp                           part2-web-1
3eebfbb2bb56   part2-db    "/bin/sh -c 'java -c…"   About an hour ago   Up About an hour   0.0.0.0:8082->8082/tcp, 0.0.0.0:9092->9092/tcp   part2-db-1
````

In the host you can open the spring web application using the following url: <br>
http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/
 <br>

You can also open the H2 console using the following url: <br>
http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console

<br> 
For the connection string use: jdbc:h2:tcp://192.168.56.11:9092/./jpadb <br>


### Publish the images (db and web) to Docker Hub ###
To publish the db image and the web image to the Docker Hub, you should run the commands: <br>

> &#36; docker login<br>
> &#36; docker tag part2-db martalopes/part2-db:latest<br>
> &#36; docker push martalopes/part2-db:latest<br>
> &#36; docker tag part2-web martalopes/part2-web:latest<br>
> &#36; docker push martalopes/part2-web:latest<br>

Now, you can share your images with others or deploy them to different environments. <br>

If you try to do this process again and encounter problems within the network, run the following commands: <br>
> &#36; docker-compose down<br>
> &#36; docker-compose build --no-cache<br>
> &#36; docker-compose up<br>

## Alternative to the presented solution ##
Kubernetes offers a compelling alternative to Docker Compose for container orchestration. <br>
While Docker Compose focuses on managing containers on a single host, Kubernetes operates on a cluster of nodes, enabling scalability and fault tolerance.<br>
Instead of using a single YAML file like Docker Compose, Kubernetes uses declarative configuration files called manifests to define the desired state of the application.<br>
In Kubernetes, Pods serve as the fundamental units of deployment, encapsulating one or more containers and shared resources. <br>
Services provide load balancing and networking capabilities, allowing seamless communication between Pods. <br>
Kubernetes offers automatic scaling based on metrics and integrates with DNS services for easy service discovery. <br>
It also provides high availability through automatic restarts and replica maintenance. <br>
Compared to Docker Compose, Kubernetes offers a broader ecosystem of plugins, tools, and integrations, making it highly adaptable to various environments. <br>
Its advanced features in scaling, load balancing, networking, and fault tolerance make it a suitable choice for managing complex deployments and production environments. <br>

## Conclusion ##
In conclusion, this work has provided valuable insights into the power and benefits of Docker and Docker Compose as essential tools for containerization and application deployment. <br>
Through the process of containerizing a Spring application, I have learned that Docker offers a lightweight and portable solution to package applications and their dependencies into self-contained units, ensuring consistency across different environments. <br>
Docker's containerization technology promotes scalability, efficiency, and reliability by isolating applications and avoiding conflicts between dependencies. <br>
Additionally, Docker Compose has proven to be a valuable companion tool, simplifying the management and orchestration of multi-container applications. <br>
With a clear and concise YAML configuration file, Docker Compose enables the definition of complex environments composed of interconnected services. <br>
This allows for the seamless integration of various components, such as databases, servers, and other dependencies, into a cohesive and easily reproducible setup.

