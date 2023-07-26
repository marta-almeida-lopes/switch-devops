# README #
This README is a simple technical report about the developed work for the Class Assignment 3 (part 2). <br>

This Class Assignment was about <strong>Virtualization with Vagrant</strong>. <br>

The goal of Part 2 of this assignment is to use Vagrant to setup a virtual environment to execute the tutorial spring boot application, gradle ”basic” version (developed in CA2, Part2). <br>

This report will cover everything that was done for this work, step by step. It will also cover the used approach with brief explanations, all the used commands, and finally, an alternative on how this work could have been done.


## CA3 - Part 2 ##

### Study the Vagrantfile and see how it is used to create and provision 2 VMs ###
> - web: this VM is used to run tomcat and the spring boot basic application 
> - db: this VM is used to execute the H2 server database

To start working on this assignment, I studied the example [vagrant multi spring tut demo](https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/) <br>
I understood that Vagrant allows to define all configuration of a Virtual Machine using a simple configuration file <br>
Here's what you should do if you want to automate the setup of one or more VMs (in this case, we automated the setup of the db and the web VMs): <br>

* To install Vagrant, you should access the website [download vagrant](https://developer.hashicorp.com/vagrant/downloads) <br>
* Then, you should download the vagrant amd64 version 2.3.4, according to your operating system <br>
  * The version is important. I've tried to do this with an outdated version, and it was not working!
* You should install the previously downloaded file
* To check if everything is ok run the following command in the terminal
&#36; vagrant --version 

* Create a directory where you will initialize a vagrant project. So, run the following commands:<br>
(Don't forget that you should be inside the CA3/ directory) <br>
&#36; mkdir Part2 <br>
&#36; cd Part2 <br>
* You should clone this project [vagrant multi spring tut demo](https://bitbucket.org/pssmatos/tut-basic-gradle.git) to have access to the Vagrantfile: <br>
&#36; git clone [vagrant multi spring tut demo](https://marta_lopes@bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo.git)
* It is possible to create a new Vagrantfile, however I found this was the easiest way to work with it. <br>
* Copy the vagrant file that is on the cloned project to CA3/Part2: <br>
(You should be on CA3/Part2/vagrant-multi-spring-tut-demo) <br>
&#36; cp Vagrantfile ..
* Be aware that in your Vagrantfile, you must change the url of the repository that you want to clone! <br>
* Your Vagrantfile should be looking like this: <br>
```
# See: https://manski.net/2016/09/vagrant-multi-machine-tutorial/
# for information about machine names on private network
Vagrant.configure("2") do |config|
config.vm.box = "ubuntu/bionic64"

# This provision is common for both VMs
config.vm.provision "shell", inline: <<-SHELL
sudo apt-get update -y
sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
openjdk-11-jdk-headless
# ifconfig
SHELL

#============
# Configurations specific to the database VM
config.vm.define "db" do |db|
db.vm.box = "ubuntu/bionic64"
db.vm.hostname = "db"
db.vm.network "private_network", ip: "192.168.56.11"

    # We want to access H2 console from the host using port 8082
    # We want to connet to the H2 server using port 9092
    db.vm.network "forwarded_port", guest: 8082, host: 8082
    db.vm.network "forwarded_port", guest: 9092, host: 9092

    # We need to download H2
    db.vm.provision "shell", inline: <<-SHELL
      wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar
    SHELL

    # The following provision shell will run ALWAYS so that we can execute the H2 server process
    # This could be done in a different way, for instance, setiing H2 as as service, like in the following link:
    # How to setup java as a service in ubuntu: http://www.jcgonzalez.com/ubuntu-16-java-service-wrapper-example
    #
    # To connect to H2 use: jdbc:h2:tcp://192.168.33.11:9092/./jpadb
    db.vm.provision "shell", :run => 'always', inline: <<-SHELL
      java -cp ./h2*.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists > ~/out.txt &
    SHELL
end

#============
# Configurations specific to the webserver VM
config.vm.define "web" do |web|
web.vm.box = "ubuntu/bionic64"
web.vm.hostname = "web"
web.vm.network "private_network", ip: "192.168.56.10"

    # We set more ram memmory for this VM
    web.vm.provider "virtualbox" do |v|
      v.memory = 1024
    end

    # We want to access tomcat from the host using port 8080
    web.vm.network "forwarded_port", guest: 8080, host: 8080

    web.vm.provision "shell", inline: <<-SHELL, privileged: false
      # sudo apt-get install git -y
      # sudo apt-get install nodejs -y
      # sudo apt-get install npm -y
      # sudo ln -s /usr/bin/nodejs /usr/bin/node
      sudo apt install -y tomcat9 tomcat9-admin
      # If you want to access Tomcat admin web page do the following:
      # Edit /etc/tomcat9/tomcat-users.xml
      # uncomment tomcat-users and add manager-gui to tomcat user

      # Change the following command to clone your own repository!
      git clone https://marta_lopes@bitbucket.org/marta_lopes/devops-22-23-maal-1221850.git
      cd devops-22-23-maal-1221850/CA3/Part2/react-and-spring-data-rest-basic
      chmod u+x gradlew
      ./gradlew clean build --refresh-dependencies
      # To deploy the war file to tomcat9 do the following command:
      pwd
      ls ./build/libs
      sudo cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /var/lib/tomcat9/webapps
    SHELL
end
end
```

* You can delete the vagrant-multi-spring-tut-demo now, since you have already copied the Vagrantfile <br>
* I also copied the project from CA2/Part2 since I didn't want to change its files. For this vagrant project, it is necessary to change some files such as the build.gradle and I didn't want to ruin a previous assignment. So, I duplicated the project and made changes only on this new version. <br>
* To do so, you want to be inside the CA3/ and you should run the command: <br>
&#36; cp -R ~/Documents/projDevops/CA2/Part2/react-and-spring-data-rest-basic/ ~/Documents/projDevops/CA3/Part2 <br>
* Now, you should access the file build.gradle that should be on CA3/Part2/react-and-spring-data-rest-basic. This is what the build.gradle should look like on the end:

```Groovy
plugins {
  id 'java'
  id 'org.springframework.boot' version '2.7.10'
  id 'io.spring.dependency-management' version '1.0.15.RELEASE'
  id 'org.siouan.frontend-jdk11' version '6.0.0'
  id 'war'
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
implementation 'org.springframework.boot:spring-boot-starter-validation'
runtimeOnly 'com.h2database:h2'
testImplementation('org.springframework.boot:spring-boot-starter-test') {
exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
}
// To support war file for deploying to tomcat
providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'
implementation 'javax.validation:validation-api:2.0.1.Final'
}

tasks.named('test') {
useJUnitPlatform()
}

task copyJar(type: Copy) {
from 'build/libs/'
into 'disc'
include '*.jar'
}
task deleteWebpack (type: Delete){
delete '/src/main/resources/static/built'
}

clean.dependsOn deleteWebpack


frontend {
nodeVersion = "14.17.3"

    System.out.println(String.format("I am running on: %s(%s)", System.getProperty("os.arch"), System.getProperty("os.name")))

    if (System.getProperty("os.arch").equals("aarch64")) {
        if (System.getProperty("os.name").equals("Linux")) {
            nodeDistributionUrlPathPattern = 'vVERSION/node-vVERSION-linux-arm64.TYPE'
        }

        if (System.getProperty("os.name").equals("Mac OS X")) {
            nodeDistributionUrlPathPattern.set("vVERSION/node-vVERSION-darwin-x64.TYPE")
        }
    }

    // See 'scripts' section in your 'package.json file'
    //cleanScript = 'run clean'
    //assembleScript = 'run assemble'
    assembleScript = 'run webpack'
    //checkScript = 'run check'

    //assembleScript = "run build"

}
```

* You should also make changes on the application.properties. It should be on the path: CA3/Part2/react-and-spring-data-rest-basic/src/main/resources/application.properties <br>
* This is what the application.properties should look like on the end:

```` JSON
server.servlet.context-path=/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT
spring.data.rest.base-path=/api
#spring.datasource.url=jdbc:h2:mem:jpadb
# In the following settings the h2 file is created in /home/vagrant folder
spring.datasource.url=jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
# So that spring will no drop de database on every execution.
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true
````

* The purpose of making changes to the application.properties file is to configure the behavior of the application, establish a connection to the H2 database, and enable the H2 console for database management. These changes define settings such as the application's base URL, database connection details, Hibernate behavior, and H2 console access. By modifying the application.properties file, the application can function correctly and interact with the desired database while providing convenient database management through the H2 console. <br>


* You should also create a ServletInitializer Class on the path: <br>
CA3/Part2/react-and-spring-data-rest-basic/src/main/java/com/greglturnquist/payroll/ <br>
* This class should look like this at the end: <br>

```` Java
package com.greglturnquist.payroll;
    
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
    
public class ServletInitializer extends SpringBootServletInitializer {
    
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(ReactAndSpringDataRestApplication.class);
  }
  
}
````

* Creating the ServletInitializer class is necessary when deploying a Spring Boot application as a WAR file on a servlet container. It ensures the proper initialization of the application in the servlet container by extending the SpringBootServletInitializer class and configuring the main class of the application. <br>


* Finally, you should also make changes on the app.js file that should be on the path: <br>
  CA3/Part2/react-and-spring-data-rest-basic/src/main/java/js/api/app.js
* You must change the componentDidMount() method: you have to update the path to get the data. In the end, it should look like this:<br>

```` Java
componentDidMount() { // <2>
client({method: 'GET', path: '/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees'}).done(response => {
this.setState({employees: response.entity._embedded.employees});
});
}
````

* This step is necessary because it updates the componentDidMount() method in the React component. The purpose of this method is to fetch data from a specific path or URL and update the component's state with the retrieved data. In this case, the path is set to /react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees, which corresponds to the endpoint for retrieving employee data from the Spring Data REST API. By updating the path in componentDidMount(), the React component will successfully retrieve and set the employee data in its state. <br>


* Finally, you should be at CA3/Part2 and if you run the following command: <br>
&#36; vagrant up <br>
* You will be able to create the two VMs.

* Now, to see the react and spring data rest application running on the browser, you can access the following url: <br>
http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/
* If you want to see the database on the browser, you should access: <br>
http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console/

* If you run the command: <br>
&#36; vagrant ssh db <br>
* You will be able to access the db VM. <br>

* If you run the command: <br>
&#36; vagrant ssh web <br>
* You will be able to access the web VM. <br>

* If you want to destroy your VMs when they are no longer needed, you just need to use: <br>
&#36; vagrant destroy <br>

## Alternative to the presented solution ##
An alternative to using Vagrant for managing virtual environments is Docker. Docker is a popular platform that helps developers package their applications and all their dependencies into portable containers.
Unlike Vagrant, which creates virtual machines, Docker containers are lighter and faster because they share the host operating system's resources. Docker containers provide a reliable and consistent environment for running applications, similar to virtual machines but with less overhead.
One of the great things about Docker is its extensive library of pre-built container images. These images make it easy to set up containers with different software and configurations, saving time and effort. Docker also offers tools for managing and scaling containers in production environments.
Docker provides a unified development and deployment experience across different machines, making it simple to share and collaborate on projects. It also makes it easier to deploy applications, especially in cloud environments or when using microservices architecture.
It's important to note that Docker and Vagrant serve different purposes. Vagrant focuses on managing virtual machines and providing consistent development environments, while Docker focuses on containerization and efficient application deployment. The choice between Vagrant and Docker depends on your project's specific needs and the level of isolation and portability required.
<br>
Another alternative would be using VirtualBox directly to setup two different VMs. All the Vagrantfile commands would have to be run inside the two VMs terminal (or via SSH), but using Vagrant for this purpose is a much automatized way of doing this.

## Conclusion ##
Through this work, I have gained valuable insights into the benefits of using Vagrant for managing virtual environments. 
Vagrant proves to be a powerful tool that simplifies the setup and configuration of development environments. Its ability to create and manage reproducible, isolated virtual machines ensures consistency across different development environments and eliminates potential conflicts.
One of the significant advantages of Vagrant is its portability. Developers can easily share their Vagrant configuration files, allowing team members to quickly set up identical development environments on their machines. This streamlines collaboration and reduces the time and effort spent on environment setup.
Vagrant integrates well with popular virtualization technologies such as VirtualBox, providing a wide range of options for running virtual machines. This compatibility enhances the versatility and adoption of Vagrant within different development workflows.
In conclusion, this work has highlighted the significant advantages of using Vagrant for managing virtual environments. Its simplicity, portability, reproducibility and scalability make it a valuable tool for developers, enabling streamlined development workflows, efficient collaboration, and consistent environment management.




