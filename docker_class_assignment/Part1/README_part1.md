# README #
This README is a simple technical report about the developed work for the Class Assignment 4 (part 1). <br>

This part of the Class Assignment was about <strong>Containers with Docker</strong>. <br>

The goal of the first part of CA4 was to practice with Docker, creating docker images and running containers using the chat application from CA2. <br>

This report will cover everything that was done for this work, step by step. It will also cover the used approach with brief explanations and all the used commands.


## CA4 - Part 1 ##
### Version 1 ###
### Creating a docker image using Dockerfile ###
A Docker image is a self-contained and portable package that includes all the necessary components to run a piece of software, such as code, dependencies, and system tools. 
Docker images are created using a Dockerfile, which is a text file that specifies the instructions to build the image. The Dockerfile typically includes commands to pull a base image, copy the application code into the image, install dependencies, configure the runtime environment, and specify the entry point for the application. <br>
To start working on this assignment, you should download [Docker for Desktop](https://www.docker.com/products/docker-desktop/) <br>
After downloading and installing Docker for Desktop, you should open the App. <br>
Then, you can open a terminal of your preference and run the following command:<br>
> &#36; docker info <br>

The docker info command is helpful for understanding the overall status of your Docker environment and gathering system-related information that can be useful for troubleshooting or managing Docker resources. <br>

Since this part of this assignment is based on a previous Class Assignment (CA2 Part 1) you should copy its contents for a new dedicated directory: CA4/Part1<br>
You should be inside the projDevops directory. <br>

> &#36; mkdir -p CA4/Part1 <br>
&#36; mkdir -p CA4/Part1 <br>
&#36; cp -r ~/Documents/projDevops/CA2/Part1/* ~/Documents/projDevops/CA4/Part1 <br>
&#36; cd CA4/Part1 <br>
&#36; touch Dockerfile <br>

Now that you created the Dockerfile, it's time to add the necessary instructions to define your Docker image: <br>
Your Dockerfile should look like this: <br>
```Docker
FROM adoptopenjdk:11 as build
COPY . /app
WORKDIR /app
COPY gradlew .
RUN chmod u+x gradlew
RUN ./gradlew build
EXPOSE 8080
CMD ./gradlew runServer
```
Here's a breakdown of the Dockerfile: <br>

* <strong>FROM adoptopenjdk:11 as build</strong> - This line specifies the base image for the Docker image, in this case, the adoptopenjdk:11 image. It will be used as the build stage. <br>
* <strong>COPY . /app</strong> - This line copies the entire content of the current directory (where the Dockerfile is located) into the /app directory in the Docker image.<br>
* <strong>WORKDIR /app</strong> - This line sets the working directory to /app inside the Docker image. All subsequent instructions will be executed from this directory. <br>
* <strong>COPY gradlew .</strong> - Here, you are specifying that a copy the gradlew file (Gradle wrapper) will be made into the current directory of the Docker image. <br>
* <strong>RUN chmod u+x gradlew</strong> - Here, you are giving the Docker image executable permissions.<br>
* <strong>RUN ./gradlew build</strong> - This line runs the ./gradlew build command inside the Docker image. It executes the Gradle build process, which will download dependencies, compile code, and build the application. <br>
* <strong>EXPOSE 8080</strong> - This line exposes port 8080 on the Docker container. It informs Docker that the application inside the container listens on port 8080. <br>
* <strong>CMD ./gradlew runServer</strong> - Finally, this line specifies the command to be executed when a container is run based on the image. It runs the ./gradlew runServer command, which starts the server or the main process of your application.

### Build the chat server ”inside” the Dockerfile ###
With this Dockerfile, you can now build a Docker image using the docker build command. <br>
Don't forget that you should be inside CA4/Part1, which means that you should be at the same level as the Dockerfile. <br>

> &#36; docker build -t chat_server_image:part1v1 . <br>

You had built a Docker image based on the Dockerfile in the current directory!

### Tag the image and publish it on Docker Hub ###
On the previous command you ran, you alreaddy tagged your image with the tag 'part1v1'. <br>
However, if you only ran:

> &#36; docker build -t chat_server_image . <br>

you could tag the image after. To do it like this, you would've need to run a second command: <br>

> &#36; docker tag chat_server_image chat_server_image:part1v1 <br>

This command adds the part1v1 tag to the existing image named chat_server_image. It renames the image with the new tag, allowing you to differentiate between different versions or variations of the image. <br>
To publish the tagged image to Docker Hub, you should create an account on Docker Hub. After doing so, you should login and enter your credentials running the following command: <br>

> &#36; docker login <br>

Then, you should tag the image with the Docker Hub repository information: <br>

> &#36; docker tag chat_server_image:part1v1 martalopes/chat_server_image:part1v1 <br>

Then, you should push the tagged image to Docker Hub: <br>

> &#36; docker push martalopes/chat_server_image:part1v1 <br>

Where it says martalopes, you should replace it with your Docker Hub username. <br>

### Execute the chat client in your host computer and connect to the chat server that is running in the container ###
If you are running the chat client on a Windows operating system on your host computer and want to connect it to the chat server running in the container, please consider the following step: <br>

* Open the file gradlew (that is inside CA4/Part1) and change the line endings from CRLF (Carriage Return + Line Feed) to LF (Line Feed) format. <br>

By converting the line endings to LF format, you ensure compatibility between the Windows operating system and the chat client. <br>
In Windows, lines in text files are typically terminated with a combination of Carriage Return (CR) and Line Feed (LF) characters (CRLF). <br>
On the other hand, Unix-based systems (including Linux and macOS) use only the Line Feed character (LF) to terminate lines. <br>
When working with code or text files, especially in a cross-platform context, it's important to ensure consistent line endings. <br>
If the line endings are not standardized, it can lead to compatibility issues and unexpected behavior when running scripts or commands. <br>
In the case of the gradlew file, it is necessary to change the line endings to LF format to ensure that the file can be correctly interpreted by Unix-based systems, such as the one running inside the Docker container. <br>
By converting the line endings, you help ensure the proper execution of the chat client and its successful connection to the chat server running in the container. <br>

After updating the gradlew file and changing the line endings, the next step is to update the build.gradle file. <br>
The changes involve updating the port numbers specified in the runClient and runServer tasks to 8080. This ensures that both the chat client and chat server are configured to communicate over port 8080. <br>
After the changes, the runClient and runServer tasks should look like this: <br>

```gradle
task runClient(type: JavaExec, dependsOn: classes) {
    group = "DevOps"
    description = "Launches a chat client that connects to a server on localhost:59001 "

    classpath = sourceSets.main.runtimeClasspath

    mainClass = 'basic_demo.ChatClientApp'

    args 'localhost', '8080'
}

task runServer(type: JavaExec, dependsOn: jar) {
    group = "DevOps"
    description = "Runs the server "

    classpath = sourceSets.main.runtimeClasspath

    mainClass = 'basic_demo.ChatServerApp'

    args '8080'
}
```

Remember to save the changes after updating the build.gradle file. These changes are necessary to align the configuration of the chat client and server with the chosen port (8080) for successful communication. <br>

Now, to finally execute the chat client in your host computer and connect to the chat server that is running in the container, you should run the command: <br>

> &#36; docker run -p 8080:8080 chat_server_image:part1v1 <br>

Then, on another terminal, please run: <br>

> &#36; ./gradlew runClient <br>

By following these steps, the chat client on your host computer will establish a connection with the chat server running in the Docker container, allowing you to participate in the chat communication. <br>

### Version 2 ###
### Build the chat server in your host computer and copy the jar file ”into” the Dockerfile ###
To explore the concept of docker images I've also created a second version. In this version 2, the approach is slightly different. <br>
Instead of including the source code in the Docker image, the chat server is built outside the Docker image on the host computer. <br>
The compiled JAR file of the chat server is then copied into the Docker image during the build process.

To do this version, you should create a new directory where you'll have a new Dockerfile.<br>
So, if you are on CA4/Part1, you must run the following commands: <br>

> &#36; mkdir v2 <br>
> &#36; cd v2 <br>
> &#36; touch Dockerfile <br>

Then, you can open your new Dockerfile and add the instructions to define your Docker image: <br>

```Docker
FROM adoptopenjdk:11 as build
COPY basic_demo-0.1.0.jar /app
WORKDIR /app
EXPOSE 8080
CMD java -cp basic_demo-0.1.0.jar basic_demo.ChatServerApp 8080
```

Here's a breakdown of the Dockerfile: <br>

* <strong>FROM adoptopenjdk:11 as build</strong> - This line specifies the base image for the Docker image, in this case, the adoptopenjdk:11 image. It will be used as the build stage. <br>
* <strong>COPY basic_demo-0.1.0.jar /app</strong> - This line copies the JAR file named basic_demo-0.1.0.jar from the host computer into the /app directory within the Docker image. The JAR file should be built and available in the host computer's file system before running the docker build command.<br>
* <strong>WORKDIR /app</strong> - This line sets the working directory to /app inside the Docker image. All subsequent instructions will be executed from this directory. <br>
* <strong>EXPOSE 8080</strong> - This line informs Docker that the container will listen on port 8080. It exposes port 8080 for network communication, allowing external connections to the chat server running inside the container. <br>
* <strong>CMD java -cp basic_demo-0.1.0.jar basic_demo.ChatServerApp 8080</strong> - Finally, this line specifies the command to be executed when a container is run based on the image. It runs the java command with the specified classpath (-cp flag) and the main class basic_demo.ChatServerApp. The argument 8080 indicates the port number on which the chat server should listen. <br>

With these instructions, the Docker image is configured to include the JAR file of the chat server and specify the necessary runtime command to run the chat server inside the container. <br>

If you want to see what are the contents inside the container, you can run the command: <br>

> &#36; docker run chat_server_image:part1v2 ls <br>

You'll be able to see: <br>

> basic_demo-0.1.0.jar
> Dockerfile

Now, to execute the program, you should run: <br>

> &#36; docker run -p 8080:8080 chat_server_image:part1v1 <br>

You'll see: <br>
> The chat server is running... <br>

Then, on your host computer you should open a terminal and run the following: <br>

> &#36; ./gradlew runClient <br>

And you'll see: <br>

> Task :compileJava UP-TO-DATE <br>
> Task :processResources UP-TO-DATE <br>
> Task :classes UP-TO-DATE <br>
> Task :runClient <br>
> BUILD SUCCESSFUL in 15s <br>
> 3 actionable tasks: 1 executed, 2 up-to-date <br>

If you wish to publish this image on Docker Hub, you must do the same that you did on Version 1: <br>

> &#36; docker tag chat_server_image:part1v2 martalopes/chat_server_image:part1v2 <br>
> &#36; docker push martalopes/chat_server_image:part1v2 <br>