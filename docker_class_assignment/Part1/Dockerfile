FROM adoptopenjdk:11 as build
COPY . /app
WORKDIR /app
COPY gradlew .
RUN chmod u+x gradlew
RUN ./gradlew build
EXPOSE 8080
CMD ./gradlew runServer