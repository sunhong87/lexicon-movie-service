FROM maven:3.9.2 AS maven

WORKDIR /usr/src/app
COPY . /usr/src/app

RUN mvn package

FROM adoptopenjdk/openjdk16:aarch64-centos-jdk-16.0.1_9
ARG JAR_FILE=movie-service-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker", "movie-service-0.0.1-SNAPSHOT.jar"]