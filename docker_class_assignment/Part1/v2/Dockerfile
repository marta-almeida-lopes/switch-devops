FROM adoptopenjdk:11 as build
COPY basic_demo-0.1.0.jar /app
WORKDIR /app
EXPOSE 8080
CMD java -cp basic_demo-0.1.0.jar basic_demo.ChatServerApp 8080
