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
