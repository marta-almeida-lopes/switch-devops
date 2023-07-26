# README #
This README is a simple technical report about the developed work for the Class Assignment 2 (part 1 and part 2). <br>

This Class Assignment was about <strong>Build Tools with Gradle</strong>. <br>

This report will cover everything that was done for this work, step by step. It will also cover the used approach with brief explanations, all the used commands, and finally, an alternative on how this work could have been done.


## CA2 - Part 1 ##

### Cloning gradle_basic_demo ###
To clone the repository I've used the following commands: <br>
&#36; mkdir ~/Documents/projDevops/CA2/Part1 <br>
&#36; mkdir clone_ca2 <br>
&#36; git clone [remote repository](https://marta_lopes@bitbucket.org/pssmatos/gradle_basic_demo.git) <br>
&#36; cd gradle_basic_demo <br>
&#36; rm -rf .git/ <br>
&#36; mv build.gradle gradle/ gradle.properties README.md src/ settings.gradle gradlew.bat gradlew ~/Documents/projDevops/CA2/Part1/ <br>
&#36; cd ~/Documents/projDevops/CA2/Part1 <br>
&#36; git add * <br>
&#36; git commit -m "Added the code of the Gradle Basic Demo into a new directory" <br>
&#36; git push origin master <br>

### Running the server and the client ###
After I successfully cloned the repository, it was time to check if I could run the server and the client.
I used the following commands: <br>

&#36; java -cp build/libs/basic_demo-0.1.0.jar basic_demo.ChatServerApp 59001 <br>
I executed this command to run the server <br>

I opened a new terminal to run the client and ran the command: <br>
&#36; ./gradlew runClient

I've inserted a valid nickname, and then I could interact with the chat.
If I open a new terminal to run other client, this new client would be added to the chat.
From the server side, this is the received message: <br>

The chat server is running... <br>
11:00:57.714 [pool-1-thread-1] INFO  basic_demo.ChatServer.Handler - A new user has joined: MARTAAA       
11:03:02.718 [pool-1-thread-2] INFO  basic_demo.ChatServer.Handler - A new user has joined: MARTA_LOPES

### Adding a new task to execute the server ###

I've added the following task to the build.gradle file: <br>

```groovy
    task runServer(type: JavaExec, dependsOn: jar) { 
    
    group = "DevOps" 

    description = "Runs the server "

    classpath = sourceSets.main.runtimeClasspath

    mainClass = 'basic_demo.ChatServerApp'

    args '59001'

    }
```

Now, when I want to run the server I don't need to use the command <br>
&#36; java -cp build/libs/basic_demo-0.1.0.jar basic_demo.ChatServerApp 59001 <br>
Instead, I can use now the command <br> &#36; ./gradlew runServer <br>

### Adding a simple unit test ###

I've added the unit test to the AppTest Class. This unit test was provided on the Class Assignment.
I've also updated the build.gradle script, adding this dependency: <br>

```groovy
    testImplementation 'junit:junit:4.12'
```

When running the command <br>
&#36; ./gradlew build <br>
This is the result:

    BUILD SUCCESSFUL in 3s
    8 actionable tasks: 2 executed, 6 up-to-date

### Adding a task of type Copy ###
This task is used to make a backup of the sources of the application. It should copy the contents of the src directory to a new backup directory. <br>
I've added the following task to the build.gradle script: <br>

```groovy
    task copyTask(type: Copy) {
    from 'src/'
    into 'build/srcCopy'
    include '**/*.java', '**/*.jsp'
    exclude { details ->
        details.file.name.endsWith('.html') &&
        details.file.text.contains('staging')
        }
    }
```

However, I've only included .jsp files. This was not the final version of the code since I've noticed some problems with this approach. <br>
I've updated this task to include .java files. By updating this and running again the command <br>
    
&#36; ./gradlew copyTask <br>

I've seen that these .java files were being copied to the build/srcCopy successfully. <br>
Then, I've edited the copyTask again and deleted the excluded types of file because I wanted to include every type of files in this backup. <br>

I've also deleted the .jsp files from the included part and inserted .xml to include these type of files, since I've noticed that the log4j2.xml file that is inside the resources directory was being excluded and I didn't want that. <br>

I've also added a build.dependsOn(copyTask) because I've noticed that when I ran the command <br>

&#36; ./gradlew build <br>

this task was not being referenced during the build, which means that if I stopped using the command <br>

&#36; ./gradlew copyTask <br>

and made changes to the project without running this command, the backup would not be updated everytime I build the project. <br>

So, I've decided that this task will run everytime I build the project. <br>

To test if this process was being handled properly, I've deleted the srcCopy directory and ran the command <br>

&#36; ./gradlew build <br>

And all the previous contents were added again. <br>

This was the final result of the copyTask: <br>

```groovy
    task copyTask(type: Copy) {
    from 'src/'
    into 'build/srcCopy'
    include '**/*.java', '**/*.xml'
    }
    build.dependsOn(copyTask)
```

### Adding a task of type Zip ###
This task is used to make an archive of the sources of the application. It should copy the contents of the src directory to a new zip file. <br>
I've added the following task to the build.gradle script: <br>

```groovy
    task zipTask(type: Zip) {
    from 'src/'
    archiveName 'srcContents.zip'
    }
```

When I ran the command <br>

&#36; ./gradlew zipTask <br>

the following message appeared on the terminal: <br>

    Deprecated Gradle features were used in this build, making it incompatible with Gradle 8.0.
    You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

So, I ran the following command: <br>
&#36; ./gradlew zipTask --warning-mode all <br>

By running the previous command, I've obtained the following information: <br>

    > Configure project :
    The AbstractArchiveTask.archiveName property has been deprecated. This is scheduled to be removed in Gradle 8.0. Please use the archiveFileName property instead.

So, I've substituted archiveName to archiveBaseName. Running the same command again, the build failed stating the following: <br>

    A problem occurred evaluating root project 'basic_demo'.
    > Could not find method archiveBaseName() for arguments [srcContents.zip] on task ':zipTask' of type org.gradle.api.tasks.bundling.Zip.

I've concluded that archiveBaseName is a property of the zip task and not a method. <br>
Apparently I was passing 'srcContents.zip' as an argument to the archiveBaseName, so I passed the name of the archive without the extension (.zip) <br>
I was also not using the assignment operator, so I was not setting the archiveBaseName property. <br>
I've made the changes and the final code of the zipTask was the following:

```groovy
    task zipTask(type: Zip) {
    from 'src/'
    archiveBaseName = 'srcContents'
    }
```

I've used the command <br>

&#36; ./gradlew zipTask <br>

And the build was now successful. When I accessed <strong>Part1/build/distributions/</strong>, I could see that a <strong>srcContents-0.1.0.zip</strong> was added to the directory.

## CA2 - Part 2 ##

### Start and configure a new gradle spring boot project ###
I've started the part 2 of this assignment by creating a new branch, using the following commands: <br>

&#36; git branch tut-basic-gradle <br>
&#36; git checkout tut-basic-gradle <br>

Then, I've used the https://start.spring.io to start a new gradle spring boot project.<br>
I've selected the Spring Boot version 3.0.5 and the Java 11 version and added the following dependencies:
 > Rest Repositories <br>
 > Thymeleaf <br>
 > JPA <br>
 > H2 <br>

Then, I've executed the following commands: <br>

&#36; mkdir CA2/Part2 <br>
&#36; unzip ~/Downloads/react-and-spring-data-rest-basic.zip -d ~/Documents/projDevops/CA2/Part2/ <br>
&#36; cd CA2/Part2/react-and-spring-data-rest-basic <br>
&#36; ./gradlew tasks <br>

As I've previously stated, I've selected the Spring Boot version 3.0.5 and the Java 11 version, however when running the last command, the build failed due to incompatibility issues. <br>
So, I did the following: <br>

&#36; vim CA2/Part2/react-and-spring-data-rest-basic/build.gradle <br>

On the vim text editor, I've edited the build.gradle script and made the following changes:

```groovy
    plugins {
	id 'java'
	id 'org.springframework.boot' version '2.6.3'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    }

    group = 'com.greglturnquist'
    version = '0.0.1-SNAPSHOT'
    sourceCompatibility = '11'
```

Then, I ran again the <br>

&#36; ./gradlew tasks <br>

and the build was successful. <br>

Then, I've deleted the src directory by using the command <br>

&#36; rm -rf src/ <br>

And then I've copied the src folder (and all its subfolders) from the tutorial's basic directory into this new folder. I've also copied the webpack.config.js and package.json files. <br>
I've made these steps using the following commands: <br>

&#36; cp -R ~/Documents/projDevops/CA1/basic/src ~/Documents/projDevops/CA2/Part2/react-and-spring-data-rest-basic/ <br>

&#36; cp -R ~/Documents/projDevops/CA1/basic/webpack.config.js ~/Documents/projDevops/CA1/basic/package.json ~/Documents/projDevops/CA2/Part2/react-and-spring-data-rest-basic/ <br>

I've also deleted src/main/resources/static/built/ since this folder should be generated from the javascrit by the webpack tool. <br>

When I tried running the command <br>

&#36; ./gradlew bootRun <br>

the build failed. The error stated "javax.validation.constaints did not exist". <br>
So, I've included the implementation 'javax.validation:validation-api:2.0.1.Final' in the build.gradle script. <br>
When running the previous command again, the build was now successful. <br>
I've accessed the web page http://localhost:8080 and the page was empty as expected.

### Adding the frontend plug-in ###
I've needed to add the frontend plug-in to the project so that gradle is also able to manage the frontend. <br>
To do so, I've added the following line to the plugin block in the build-gradle script: <br>

```groovy
    id 'org.siouan.frontend-jdk11' version '6.0.0'
```

I've also added the following code in the build.gradle script to configure the previous plug-in: <br>

```groovy
    frontend {
    nodeVersion = "14.17.3"
    assembleScript = "run build"
    cleanScript = "run clean"
    checkScript = "run check"
    }
```

### Updating the scripts section in package.json ###
I've updated the scripts section in package.json file to configure the execution of webpack by adding the following code: <br>

```json
    "scripts": {
    "webpack": "webpack",
    "build": "npm run webpack",
    "check": "echo Checking frontend",
    "clean": "echo Cleaning frontend",
    "lint": "echo Linting frontend",
    "test": "echo Testing frontend"
    },
```
Then, I've executed <br>

&#36; ./gradlew build <br>

and the build was successful. <br>

Then I've executed the application using <br>

&#36; ./gradlew bootRun <br>

I've accessed the webpage http://localhost:8080 and I could see the entries I've added to the project on the first Class Assignment (CA1). <br>

### Adding the copyJar task ###
This task is used to copy the generated jar to a folder named "dist", located at the project root folder level. <br>
I've added the following task to the build.gradle script: <br>

```groovy
    task copyJar(type: Copy) {
    from('build/libs') // path to the directory where the jar files are generated
    into('dist') // path to the dist directory that will be located at the project root level
    include('*.jar')
    }
    build.dependsOn copyJar
```

When running the command <br>
&#36; ./gradlew copyJar <br>
this task was successfully executed as I saw the dist directory being correctly created at the project root. Inside this directory, all the jar files were correctly added to it. <br>

When running <br>
&#36; ./gradlew build <br>
the application build successfully as well. <br>
However, during the build, I've noticed that there were error messages saying that the copyJar task was dependant of bootJar and jar. So I've added the dependencies: <br>
```groovy
    copyJar.dependsOn jar
    copyJar.dependsOn bootJar
```
So the error was solved. <br>

### Adding the cleanWebpack task ###
This task is used to delete all the files generated by webpack (usually located at src/resources/main/static/built/). <br>
This task should be executed automatically by gradle before the clean task which is a gradle built-in task. <br>

I've added the following task to the build.gradle script: <br>
```groovy
    task cleanWebpack(type: Delete) {
    description = 'Deletes all generated files by webpack'
    delete fileTree(dir: "${projectDir}/src/main/resources/static/built/", include: ['**/*.js', '**/*.js.map'])
       doLast {
          println 'Webpack files deleted'
       }
    }
```
I've added a print 'Webpack files deleted' to check during the build (and before the clean task), if the .js and .js.map files were successfully deleted, and they were. <br>
The build was again successful. <br>

## Alternative to the presented solution ##
Instead of using Gradle, I could've used a Maven which is a popular build automation tool primarily used for Java projects.
Maven is another widely used build tool that provides similar functionality to Gradle but follows a different approach in terms of configuration and conventions.
Here are some key points to consider when comparing Gradle and Maven: <br>

<strong>Maven advantages</strong> <br>
* Convention-over-configuration: Maven follows a strict convention for project structure, build lifecycle and default settings, which can be advantageous for developers who prefer a standardized approach. <br>
* Centralized dependency management: Maven uses a central repository  for managing dependencies, making it easier to manage and share dependencies across different projects. <br>
* XML-based configuration: Maven uses XML for configuration, which can be helpful for developers who are familiar with XML and prefer a declarative approach. <br>
* Maven has been around for a longer time and has a large, mature ecosystem with extensive documentation, plugins, and community support. <br>
<br>

<strong>Maven disadvantages</strong> <br>
* Less flexibility: Maven has a more rigid build lifecycle and may require more configuration for custom build scenarios compared to Gradle, which allows more customization. <br>
* Maven's XML-based configuration and strict conventions can make it more challenging to set up and configure compared to Gradle's Groovy-based configuration and flexibility. <br>
* XML-based configuration in Maven can be more verbose compared to Gradle's Groovy-based configuration, which may result in larger and more complex configuration files. <br>

The choice between Gradle and Maven depends on the specific needs and preferences of a project and the team. Both tools have their strengths and weaknesses, and the decision should be based on factors such as project requirements, team expertise, and existing infrastructure. <br>

<strong>Implementing the Maven alternative</strong> <br>
* To build a Maven project, I could use the <strong>&#36; mvn clean install</strong> command to clean the project, compile the source code and run tests. To successfully build a Maven project, it would be necessary to add this to the pom.xml: <br>
```xml
      <build>
        <plugins>
            <!-- Required for compiling the project usign maven -->
            <plugin><!-- Compiler configuration-->
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                    <showWarnings>false</showWarnings>
                    <compilerArgs>
                    </compilerArgs>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>
        </plugins>
      </build>
```

* To manage the project dependecies, Maven uses a pom.xml. In this file, I could specify dependencies and Maven will automatically download them and manage them from remote repositories. An example of defining dependencies on pom.xml would be something like this: <br>
```xml
      <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.9.1</version>
            <scope>test</scope>
        </dependency>
      </dependencies>
```

* To customize the build, Maven has a predefined build lifecycle, having phases such as "compile", "test" and "package". The build process is customizable by binding plug-ins to these lifecycle phases in the pom.xml file. <br>
* To run the tests, Maven has a built-in support for running tests using plug-ins like Surefire or Failsafe. It is possible to configure test-related settings in the pom.xml, such as test directories, test frameworks and test reports. This can be configured in the <build> section of our projects' pom.xml file, like this: <br>
```xml
      <plugin>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.22.2</version>
        <configuration>
            <!-- new configuration needed for coverage per test -->
            <properties>
                <property>
                    <name>listener</name>
                    <value>org.sonar.java.jacoco.JUnitListener</value>
                </property>
            </properties>
        </configuration>
      </plugin>
```

* To build multimodule projects, Maven allows us to define parent-child relationships between projects in the pom.xml file and build them alltogether as a single unit. An example would be like this: <br>

```xml
      <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.2.4.RELEASE</version>
          <relativePath/>
      </parent>
```
## Conclusion ##
This assignment was an excellent opportunity to learn more about Gradle and how it can be used to automate build processes. 
It was also an opportunity to practice using Git and solidify what we've learned in the previous Class Assignment.<br>

In conclusion, I have learned that Gradle is a grat build automation tool and a nice alternative to Maven. <br>
Gradle can simplify the process of building software projects. With its flexible and highly customizable approach, Gradle can accommodate a wide range of project structures and build requirements, making it a popular choice for many development teams.<br>
Through my exploration of Gradle's key features and functionality, I have gained a better understanding of how it can be used to manage dependencies, run tests, add tasks and how they interact with each other.<br>
Overall, I am impressed by the versatility and ease of use of Gradle, and I look forward to applying my newfound knowledge to future projects.

