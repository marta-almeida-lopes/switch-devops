# README #
This README is a simple technical report about the developed work for the Class Assignment 5. <br>

This Class Assignment was about <strong>CI/CD Pipelines with Jenkins</strong>. <br>

The primary goal of CA5 was to gain hands-on experience with Jenkins by working on the "gradle basic demo" project, which is already available in our personal repository. The initial task involved setting up a straightforward pipeline in Jenkins. <br>
After familiarizing ourselves with the "gradle basic demo" project, we proceeded to create another pipeline in Jenkins specifically designed to build the tutorial spring boot application, using the "basic" version of Gradle (as developed in CA2, Part2). <br>

This report will cover everything that was done for this work, step by step. It will also cover the used approach with brief explanations and all the used commands.

## CA5 ##
### Create a very simple pipeline for CA2/Part1 ###
Jenkins is an open-source automation tool widely used for continuous integration and continuous delivery (CI/CD) of software projects. It provides a flexible and extensible platform that helps automate various stages of software development, such as building, testing, and deploying applications. <br>
One of the key features of Jenkins is its support for pipelines. A pipeline in Jenkins is a set of instructions that define the steps and actions required to build, test, and deploy software. <br>

To start working on this assignment, you should install Jenkins. <br>
You should open a terminal of your preference and run the following command to pull the Jenkins Docker image: <br>

> &#36; docker pull jenkins/jenkins <br>

If you are working on a Windows operating system, open a command prompt and execute the following command:<br>

> &#36; docker run -d -p 8080:8080 -p 50000:50000 -v /var/run/docker.sock:/var/run/docker.sock -v jenkins-data:/var/jenkins_home --name jenkins jenkins/jenkins:lts <br>

By following these steps, you will  set up a Docker container for it. This will enable you to access Jenkins through a web browser and begin working on the assignment. <br>

Please note that the following  commands assume a Unix-like environment. If you are using a different operating system, make sure to adjust the commands accordingly. <br> 

Now if you type <strong>localhost:8080</strong> you'll be able to see the Jenkins UI. You'll need to insert the Administrator password to unlock Jenkins. This password appeared when you first ran the docker run command where you specified the ports and set up the Docker container. If you missed the password, you just need to run the command: <br>

> docker logs [container-id]

If you're unsure about the container ID, run: <br>

> docker ps

This will display the running Docker containers along with their IDs. <br>
After entering the Administrator password, follow the on-screen instructions to create your Jenkins account. <br>
By completing these steps, you'll have Jenkins installed and ready to use. Now you can proceed with creating a simple pipeline for CA2/Part1. <br>

Now, go to the Jenkins dashboard and: <br>
* Click on "New item" to create a new item.
* Select "Pipeline" and provide a name for your pipeline.
* Choose "Pipeline script" and paste it into the script editor.

and then insert this: <br> 

```Jenkins
pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        echo 'Checking out...'
         git 'https://marta_lopes@bitbucket.org/marta_lopes/devops-22-23-maal-1221850'
      }
    }
    stage('Assemble') {
      steps {
        echo 'Building...'
        dir('CA2/Part1') {
          sh 'chmod +x gradlew'
          sh './gradlew clean assemble'
        }
      }
    }
    
    stage('Test') {
        steps {
            echo 'Running tests...'
            dir('CA2/Part1') {
            sh 'chmod +x gradlew'
            sh './gradlew test'
            }
        }
    }

stage('Archiving') {
      steps {
        echo 'Archiving...'
        archiveArtifacts artifacts: 'CA2/Part1/build/libs/*'
      }
    }
  }
}
```
Here's a breakdown of this script: <br>

* <strong>Checkout</strong> - The checkout stage is responsible for retrieving the source code from the specified repository using Git. <br>
  * The git command is used to clone the repository located at the URL provided. <br>
  * The echo command is used to display the message "Checking out..." as a status update during the execution of this stage. <br>
* <strong>Assemble</strong> - The assemble stage is responsible for building the project located in the CA2/Part1 directory. <br>
  * The dir command is used to navigate to the specified directory within the workspace where the Jenkins job is running. <br>
  * The sh command is used to execute shell commands within the specified directory. <br>
  * The chmod command sets the executable permission for the gradlew script, allowing it to be run. <br>
  * The ./gradlew clean assemble command executes the Gradle wrapper script (gradlew) with the clean assemble tasks, which perform a clean build of the project. <br>
  * The echo command displays the message "Building..." as a status update during the execution of this stage. <br>
* <strong>Test</strong> - The test stage is responsible for running the tests for the project. <br>
  * Similar to the previous stage, the dir command navigates to the CA2/Part1 directory. <br>
  * The sh command sets the executable permission for gradlew and executes the test task using gradlew. <br>
  * The echo command displays the message "Running tests..." as a status update during the execution of this stage. <br>
* <strong>Archiving</strong> - The archiving stage is responsible for archiving the built artifacts of the project. <br>
  * The archiveArtifacts step is a Jenkins-specific step that archives files for later use or analysis. <br>
  * The artifacts parameter specifies the path to the artifacts that need to be archived. <br>
  * In this case, the path CA2/Part1/build/libs/* is specified, which includes all the files within the libs directory as artifacts to be archived. <br>
  * The echo command displays the message "Archiving..." as a status update during the execution of this stage. <br>

These stages work together to create a continuous integration pipeline that fetches the source code, builds the project, runs tests, and archives the resulting artifacts. <br>
Each stage represents a specific phase of the software development process and helps ensure that the project is built and tested consistently.

### Do the same but now with a Jenkinsfile ###
Instead of directly specifying a script, you have the option to create a pipeline that utilizes a Jenkinsfile. <br>
A Jenkinsfile is a text file that contains the declarative or scripted syntax for defining the entire pipeline in code. <br>
To create a pipeline using a Jenkinsfile, you can follow these steps: <br>

* Create a file named Jenkinsfile (or any other name you prefer) in your source code repository: <br>
> &#36; touch Jenkinsfile
* Open the Jenkinsfile in a text editor and define your pipeline using either declarative or scripted syntax: <br>

```Jenkins
pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        echo 'Checking out...'
        git url: 'https://bitbucket.org/marta_lopes/devops-22-23-maal-1221850'
      }
    }

    stage('Assemble') {
      steps {
        echo 'Building...'
        dir('CA2/Part1') {
          sh 'chmod +x gradlew'
          sh './gradlew clean assemble'
        }
      }
    }

    stage('Test') {
      steps {
        echo 'Running tests...'
        dir('CA2/Part1') {
          sh 'chmod +x gradlew'
          sh './gradlew test'
        }
      }
    }

    stage('Archiving') {
      steps {
        echo 'Archiving...'
        archiveArtifacts artifacts: 'CA2/Part1/build/libs/*'
      }
    }
  }
}
```
Since this repository is public, you don't have to worry about credentials id... Yet. <br>

* Commit and push the Jenkinsfile to your source code repository. <br>

> &#36; git add Jenkinsfile <br>
> &#36; git commit-m "#issueNumber - Added a Jenkinsfile to the repository" <br>
> &#36; git push origin master

* In Jenkins, create a new pipeline job and specify that it should read the pipeline configuration from the Jenkinsfile located in your repository. <br>

But we'll have time to explore this a little better onto the next part (CA2/Part2).


### Create a fork of your repository and make it private ###
Forking a repository is an important feature of version control systems like Git, and it offers numerous advantages in software development workflows. <br>
Forking allows developers to create an independent copy of a repository, which can be modified and experimented with without affecting the original project. <br>
To fork your repository you should go to your Bitbucket repository and click on "Fork this repository". <br>
Give it a name and select the option to make it private. This ensures that only authorized individuals can access it. <br>
Then clone it to your local machine and let's start working on this forked repository.<br>

> &#36; mkdir projDevopsJenkins <br>
> &#36; cd projDevopsJenkins <br>
> &#36; git clone [forked-repository] <br>

### Create an App password on Bitbucket ###
An App password is a secure authentication credential that can be generated and used specifically for integrating third-party applications or services with your Bitbucket account. <br>
It provides a way to grant limited access to your Bitbucket resources without exposing your main account password. <br>
To create an App password on Bitbucket, follow these steps: <br>

* Log in to your Bitbucket account. <br>
* Click on your profile picture. <br>
* Look for Personal settings. <br>
* Look for the App password section. <br>
* Click on the option to create a new App password. <br>
* Provide a label for the App password to help you identify its purpose. <br>
* Choose the specific permissions or access rights that you want to grant to the App password. This allows you to control what actions the third-party application or service can perform. <br>
* Generate the App password. <br>
<br>
Once you have created the App password, Bitbucket will provide you with the generated credentials, typically consisting of a username and a randomly generated password. Keep this password, it will be needed.<br>

### Specify a credential id on Jenkins ###
Since we now want Jenkins to use our new private repository, we need to tell Jenkins what credentials to use when accessing our repository. <br>
* Open Jenkins and navigate to the dashboard. <br>
* Click on "Credentials" in the main menu. <br>
* On the left side, click on "System" to access global credentials settings. <br>
* Look for the "Global credentials (unrestricted)" section and click on it. <br>
* Insert the Bitbucket's password that I told you to keep and specify id such as: <br>
  * mal-bitbucket-credentials <br>

By specifying the credential ID in Jenkins, you can reference it in your Jenkinsfile or pipeline configuration to authenticate and access your private Bitbucket repository. <br>
Make sure to use the same credential ID in your Jenkinsfile when interacting with the repository. This allows Jenkins to securely access the repository using the provided credentials. <br>

### Create a new Job of type Pipeline ###
* In the configuration select Pipeline script from SCM instead of Pipeline script. <br>
* Select git and enter the URL of the new private repository and select the Credentials. <br>

By configuring the job in this way, Jenkins will automatically fetch the pipeline script from the specified repository whenever the job is triggered. <br>
This allows you to version and manage your pipeline code alongside your project code, making it easier to track changes and collaborate with others on your pipeline configuration.

### Create a Jenkinsfile for CA2/Part2 ###
On this Jenkinsfile, you should define the following stages: <br>
* <strong>Checkout</strong> - To check out the code from the repository. <br>
* <strong>Assemble</strong> - Compiles and Produces the archive files with the application. Do not use the build task of gradle (because it also executes the tests)! <br>
* <strong>Test</strong> - Executes the Unit Tests and publish in Jenkins the Test results. See the junit step for further information on how to archive/publish test results.<br>
* <strong>Javadoc</strong> - Generates the javadoc of the project and publish it in Jenkins. See the publishHTML step for further information on how to archive/publish html reports.<br>
* <strong>Archive</strong> - Archives in Jenkins the archive files (generated during Assemble, i.e., the war file) <br>
* <strong>Publish Image</strong> - Generate a docker image with Tomcat and the war file and publish it in the Docker Hub.<br>

This was what the final Jenkinsfile looked like: <br>

```Jenkins
pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        echo 'Checking out...'
         git credentialsId: 'mal-bitbucket-credentials', url: 'https://marta_lopes@bitbucket.org/marta_lopes/devops-22-23-maal-1221850-jenkins'
      }
    }
    
    stage('Build') {
      steps {
        echo 'Building...'
        dir('CA2/Part2/react-and-spring-data-rest-basic') {
          sh 'chmod +x gradlew'
          sh './gradlew clean assemble'
        }
      }
    }

    stage('Test') {
        steps {
            echo 'Running tests...'
            dir('CA2/Part2/react-and-spring-data-rest-basic') {
            sh 'chmod +x gradlew'
            sh './gradlew test'
            }
        }
    }

    stage('Generate Javadoc') {
        steps {
            echo 'Generating Javadoc...'
            dir('CA2/Part2/react-and-spring-data-rest-basic') {
            sh 'chmod +x gradlew'
            sh './gradlew generateJavadoc'
            }
            archiveArtifacts artifacts: 'CA2/Part2/react-and-spring-data-rest-basic/build/javadoc/**/*', fingerprint: true
        }
    }

    stage('Archiving') {
        steps {
            echo 'Archiving...'
            archiveArtifacts artifacts: 'CA2/Part2/react-and-spring-data-rest-basic/build/libs/*'
        }
    }

    stage('Publish Image') {
        steps {
            echo 'Publishing Docker image...'
            dir('CA2/Part2/react-and-spring-data-rest-basic') {
                withCredentials([usernamePassword(credentialsId: 'mal-dockerhub-credentials', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    sh "docker build -t martalopes/tomcat:${env.BUILD_NUMBER} ."
                    sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
                    sh "docker push martalopes/tomcat:${env.BUILD_NUMBER}"
                    sh "docker logout"
                }
            }
        }
    }
}

post {
  always {
    publishHTML(target: [
      allowMissing: false,
      alwaysLinkToLastBuild: false,
      keepAll: true,
      reportDir: 'CA2/Part2/react-and-spring-data-rest-basic/build/javadoc',
      reportFiles: 'index.html',
      reportName: 'Javadoc'
    ])
  }
}

    }
```
The major differences between the first Jenkinsfile and this one are the use of the credentialsId, the stage Generate Javadoc, the stage Publish Image and the post. <br>
It's important that you also access your Jenkins on localhost:8080 and install the following plugins: <br>

* HTML Publisher <br>
* Docker plugin <br>
* Docker pipeline <br>

If you don't install these plugins, Docker-related functionalities may not work out as expected and the Javadoc will not be generated for sure. <br>

#### About the war files and the Javadoc ####
War files must be generated on the Assemble stage. If this is not happening for you, you may need to change your build.gradle file to include the plugin id war. <br>
After making this change, you can run the command: <br>

> &#36; ./gradlew bootWar <br>

and if you access the build/libs directory, you'll be able to see that now you have war files. <br>
If you execute the following command: <br>

> &#36; ./gradlew clean assemble <br>

It will also work, since the war files are generated on the Assemble stage. <br>

This is what my build.gradle file ended looking like: <br>

```Gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '2.6.3'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'org.siouan.frontend-jdk11' version '6.0.0'
    id 'war'
}

frontend {
    nodeVersion = "14.17.3"
    assembleScript = "run build"
    cleanScript = "run clean"
    checkScript = "run check"
}

group = 'com.greglturnquist'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-data-rest'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    runtimeOnly 'com.h2database:h2'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    implementation 'javax.validation:validation-api:2.0.1.Final'
}

task copyJar(type: Copy) {
    from('build/libs') // path to the directory where the jar files are generated
    into('dist') // path to the dist directory that will be located at the project root level
    include('*.jar')
}

task cleanWebpack(type: Delete) {
    description = 'Deletes all generated files by webpack'
    delete fileTree(dir: "${projectDir}/src/main/resources/static/built/", include: ['**/*.js', '**/*.js.map'])
    doLast {
        println 'Webpack files deleted'
    }
}

build.dependsOn copyJar
copyJar.dependsOn jar
copyJar.dependsOn bootJar
clean.dependsOn cleanWebpack

tasks.named('test') {
    useJUnitPlatform()
}

task generateJavadoc(type: Javadoc) {
    source = sourceSets.main.allJava
    classpath += configurations.compileClasspath
    options.encoding = 'UTF-8'
    options.charSet = 'UTF-8'
    options.author = true
    options.version = project.version
    options.links('https://docs.oracle.com/javase/8/docs/api/')
    destinationDir = file("$buildDir/javadoc")
}
```

You may notice that I've added a generateJavadoc task of Javadoc type. I've added this task to ensure that when building the project, a Javadoc would be generated. <br>
However, I'me unsure about the need of adding this task, so you may skip this step. <br>
If you add this task to your build.gradle file, run the command: <br>

> &#36; gradlew generateJavadoc <br>

And you'll be able to see the Javadoc files on the build directory. <br>

#### About the part where we must Publish an Image ####
This part is where everything may seem a bit trickier. <br>
Your pipeline on Jenkins must be able to automatically generate a docker image with Tomcat and the war file of you project, and publish it in the Docker Hub. <br>
First, you must tell Jenkins how it can access to your Dockerhub. Similarly to what we have done with Bitbucket credentials, you must set up Docker Hub Credentials in Jenkins: <br>

* Go to your Jenkins and navigate to the Credentials section. <br>
* Click on "System" in the sidebar and then "Global credentials (unrestricted)". <br>
* Click on "Add Credentials" to create new Docker Hub credentials. <br>
* Enter your Docker Hub username and password, and provide an ID for the credentials (e.g., dockerhub-credentials). <br>
* Save the credentials. <br>

After this, you must create a Dockerfile for the Tomcat. This is what it should look like: <br>

```Docker
FROM tomcat:9.0.63-jdk11-openjdk-buster

COPY build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
```

* The FROM instruction specifies the base image for your Docker image. In this case, it uses the official Tomcat image with the tag 9.0.63-jdk11-openjdk-buster. This base image provides the necessary environment for running Tomcat. <br>
* The COPY instruction copies the WAR file from the specified source path (build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war) to the destination path within the Docker image (/usr/local/tomcat/webapps/). This is the default location where Tomcat looks for web applications to deploy.

The Dockerfile must be created by the build.gradle file level. You may create this file running the command: <br>

> &#36; touch Dockerfile <br>

and edit it on a text editor. <br>

Finally, you must be sure that your Jenkins container has Docker installed. <br>
To do so, you must access the container via ssh: <br>

> &#36; docker ps <br> 
* To know the container id. <br>

> &#36; docker exec -it [container-id] bash <br>
* To access the container where Jenkins is running. <br>

> &#36; docker --version <br>
* To see the Docker's version. If you get a "not found" message, your container does not have Docker installed. <br>

So, you have to install it. Execute the following commands: <br>

> &#36; exit <br>
* To exit the container. <br>

> &#36; docker exec -u 0 -it [container-id] bash <br>
* To access the container with root privileges. <br>

> &#36; apt update <br>
* To update the local package index of your system. <br>

> &#36; apt install docker.io <br>
* To install Docker. <br>

> &#36; usermod -aG docker jenkins <br>
* Adds Jenkins to the docker group. This is necessary to grant the Jenkins access to the Docker daemon, allowing it to interact with Docker and run Docker commands. <br>

> &#36; chmod 666 /var/run/docker.sock <br>
* Changes the permissions of the Docker socket file located at /var/run/docker.sock. The Docker socket file is used for communication between Docker clients (such as the Jenkins Docker plugin) and the Docker daemon. <br>
Setting the permissions to 666 allows both the owner and group members to read from and write to the socket file, while also allowing other users to read from and write to it. <br>
This ensures that the Jenkins user can communicate with the Docker daemon via the socket file. <br>

> &#36; exit <br>
* To exit the container. <br>

> &#36; docker exec -it [container-id] bash <br>
* To access the container without root or sudo privileges. <br>

> &#36; docker --version <br>
* To check if Docker is already installed (it must be!) <br>

> &#36; docker login <br>
* To authenticate with a Docker registry (Dockerhub). When you run this command, it prompts you to enter your Dockerhub username and password. <br>
This authentication is necessary before you can push or pull images from the registry.

If you were able to log in and run all the previous commands, Docker is definitelly installed on Jenkins and you will have all the needed permissions. <br>

So let's take a step back and assess the Publish Image stage on the Jenkinsfile: <br>

```Jenkins
stage('Publish Image') {
        steps {
            echo 'Publishing Docker image...'
            dir('CA2/Part2/react-and-spring-data-rest-basic') {
                withCredentials([usernamePassword(credentialsId: 'mal-dockerhub-credentials', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    sh "docker build -t martalopes/tomcat:${env.BUILD_NUMBER} ."
                    sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
                    sh "docker push martalopes/tomcat:${env.BUILD_NUMBER}"
                    sh "docker logout"
                }
            }
        }
    }
```

Here's a breakdown of these steps: <br>

* The dir('CA2/Part2/react-and-spring-data-rest-basic') block sets the working directory to the specified path, where your project's files are located. <br>
* The withCredentials block allows you to access the Docker Hub credentials you configured in Jenkins earlier. It retrieves the username and password from the specified credentials ID ('mal-dockerhub-credentials') and assigns them to the environment variables $DOCKERHUB_USERNAME and $DOCKERHUB_PASSWORD, respectively. These environment variables will be used later to authenticate with Docker Hub. <br>
* The docker build -t martalopes/tomcat:${env.BUILD_NUMBER} . command builds a Docker image based on the Dockerfile in the current directory (.). The -t flag tags the image with a name and version. In this case, the image is tagged as martalopes/tomcat with the version being the Jenkins build number (${env.BUILD_NUMBER}). This ensures a unique image tag for each build. <br>
* The docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD command authenticates with Docker Hub using the previously retrieved Docker Hub credentials. It uses the $DOCKERHUB_USERNAME and $DOCKERHUB_PASSWORD environment variables as the username and password, respectively. <br>
* The docker push martalopes/tomcat:${env.BUILD_NUMBER} command pushes the built Docker image to Docker Hub. It specifies the image name and version to be pushed (martalopes/tomcat:${env.BUILD_NUMBER}). This makes the image accessible in your Docker Hub repository. <br>
* Finally, the docker logout command logs out from Docker Hub, ensuring the Jenkins environment is clean and no longer authenticated with Docker Hub. <br> 

By following these steps, your Jenkins pipeline will automatically build the Docker image, tag it with a unique version, authenticate with Docker Hub, push the image to Docker Hub, and then log out. <br>
This enables the continuous integration and deployment of your project as a Docker image. <br>

Now you should build the pipeline and get something like this on your console output: <br>

```Console
Started by user Marta Almeida Lopes
Obtained Jenkinsfile from git https://marta_lopes@bitbucket.org/marta_lopes/devops-22-23-maal-1221850-jenkins
[Pipeline] Start of Pipeline
(...)
using GIT_ASKPASS to set credentials MAL Bitbucket Credentials
 > git fetch --tags --force --progress -- https://marta_lopes@bitbucket.org/marta_lopes/devops-22-23-maal-1221850-jenkins +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
Checking out Revision 7471a4d7bb32d0f6e03b5043e8c908a300d369fc (refs/remotes/origin/master)
(...)
[Pipeline] { (Checkout)
[Pipeline] echo
Checking out...
(...)
+ chmod +x gradlew
[Pipeline] sh
+ ./gradlew clean assemble
Starting a Gradle Daemon (subsequent builds will be faster)
> Task :installNode UP-TO-DATE
> Task :installYarnGlobally SKIPPED
> Task :enableYarnBerry SKIPPED
> Task :installYarn SKIPPED
> Task :installFrontend
(...)
> Task :clean

> Task :assembleFrontend

> spring-data-rest-and-reactjs@0.1.0 build /var/jenkins_home/workspace/script from SCM/CA2/Part2/react-and-spring-data-rest-basic
> npm run webpack
(...)
> Task :compileJava
> Task :processResources
> Task :classes
> Task :bootWarMainClassName
> Task :bootWar
> Task :war
> Task :assemble
(...)
BUILD SUCCESSFUL in 50s
(...)
[Pipeline] { (Test)
[Pipeline] echo
Running tests...
[Pipeline] dir
Running in /var/jenkins_home/workspace/script from SCM/CA2/Part2/react-and-spring-data-rest-basic
[Pipeline] {
[Pipeline] sh
+ chmod +x gradlew
[Pipeline] sh
+ ./gradlew test
> Task :compileJava UP-TO-DATE
> Task :processResources UP-TO-DATE
> Task :classes UP-TO-DATE
> Task :compileTestJava
> Task :processTestResources NO-SOURCE
> Task :testClasses
> Task :test
(...)
BUILD SUCCESSFUL in 6s
4 actionable tasks: 2 executed, 2 up-to-date
(...)
[Pipeline] { (Generate Javadoc)
[Pipeline] echo
Generating Javadoc...
[Pipeline] dir
Running in /var/jenkins_home/workspace/script from SCM/CA2/Part2/react-and-spring-data-rest-basic
[Pipeline] {
[Pipeline] sh
+ chmod +x gradlew
[Pipeline] sh
+ ./gradlew generateJavadoc
> Task :generateJavadoc
(...)
BUILD SUCCESSFUL in 5s
1 actionable task: 1 executed
(...)
[Pipeline] { (Publish Image)
[Pipeline] echo
Publishing Docker image...
[Pipeline] dir
Running in /var/jenkins_home/workspace/script from SCM/CA2/Part2/react-and-spring-data-rest-basic
[Pipeline] {
[Pipeline] withCredentials
Masking supported pattern matches of $DOCKERHUB_PASSWORD
[Pipeline] {
[Pipeline] sh
+ docker build -t martalopes/tomcat:24 .
#1 [internal] load .dockerignore
#1 sha256:c8cf1a7dd0b58fcfaa5b1fc035a78100adc28be153fa70b6b29a308c63f26995
#1 transferring context:
#1 transferring context: 2B 0.0s done
#1 DONE 0.2s

#2 [internal] load build definition from Dockerfile
#2 sha256:d79ec0c10526624030a1410447dd967cb00af3c2300b5ba3db463ff12f9da864
#2 transferring dockerfile: 180B 0.0s done
#2 DONE 0.2s

#3 [internal] load metadata for docker.io/library/tomcat:9.0.63-jdk11-openjdk-buster
#3 sha256:c6ade6c8f8c16f042e08d7960f4d208ed3ea27ff215e35553113d6f5c8300e51
#3 ...

#4 [auth] library/tomcat:pull token for registry-1.docker.io
#4 sha256:6c7a758f0c07caef88d0540b11e725380471b1b7485ce0b579ff4690875f4ea9
#4 DONE 0.0s

#3 [internal] load metadata for docker.io/library/tomcat:9.0.63-jdk11-openjdk-buster
#3 sha256:c6ade6c8f8c16f042e08d7960f4d208ed3ea27ff215e35553113d6f5c8300e51
#3 DONE 2.2s

#5 [1/2] FROM docker.io/library/tomcat:9.0.63-jdk11-openjdk-buster@sha256:b9c619be8239555bedf14a4830464e427532fd2fedfdfb2b40fca434591445b8
(...)

#7 [2/2] COPY build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
#7 sha256:4688a300bd712c3af84e684e7031e9e0aae56657e737e181adb2b3a8f583805c
#7 DONE 0.4s

#8 exporting to image

(...)
Login Succeeded
[Pipeline] sh
+ docker push martalopes/tomcat:24
The push refers to repository [docker.io/martalopes/tomcat]
aa1b4906361c: Preparing
(...)
cd67577bf5dc: Mounted from library/tomcat
(...)
docker logout
(...)
[htmlpublisher] Archiving at BUILD level /var/jenkins_home/workspace/script from SCM/CA2/Part2/react-and-spring-data-rest-basic/build/javadoc to /var/jenkins_home/jobs/script from SCM/builds/24/htmlreports/Javadoc
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```

Then, if the build was successful, you'll be able to access your Dockerhub and see that the Tomcat image is there. <br>

### Conclusion ###
Jenkins' flexibility make it a popular choice for CI/CD workflows. <br>

Throughout this project, I have gained valuable insights and skills in utilizing Jenkins and Docker. By incorporating Jenkins into the workflow, I learned the importance of continuous integration and continuous delivery (CI/CD) practices, enabling automated building, testing, and deployment of software. <br>

One of the key advantages of using Jenkins is its flexibility and extensibility. It supports a wide range of plugins and integrations with various tools and technologies, allowing seamless integration with other systems and enabling customization to fit specific project requirements. <br>

Integrating Docker with Jenkins further enhanced the project's efficiency. Docker's containerization technology provided a consistent and isolated environment for running applications, ensuring reproducibility and eliminating dependency issues. By utilizing Docker, I could package the application along with its dependencies into a portable container, ensuring consistent behavior across different environments. <br>

The combination of Jenkins and Docker made my work easier by automating various tasks, such as building and testing, reducing manual effort, and enabling faster iterations. <br>

As for alternatives, other CI/CD platforms like GitLab CI/CD could be used instead of Jenkins. Similarly, alternative containerization platforms like Kubernetes could replace Docker, depending on specific project requirements and infrastructure preferences. <br>

To further enhance the project, Docker Compose could be employed. Docker Compose allows defining and managing multi-container applications, specifying dependencies, networking, and other configurations in a declarative manner. It simplifies the management of interconnected services, enabling easier orchestration and deployment of complex applications. <br>

Overall, this work with Jenkins and Docker has equipped me with valuable skills in modern software development practices, empowering me to efficiently build, test, and deploy applications, while ensuring scalability, portability, and maintainability.