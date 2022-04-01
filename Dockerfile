FROM maven:3.8.4-jdk-8-slim AS build
COPY src worldClassApp/src
COPY pom.xml worldClassApp/
RUN mvn -f worldClassApp/pom.xml clean package -DskipTests=true

FROM openjdk:8-jre-slim

EXPOSE 8090

RUN mkdir /WCApp

COPY --from=build worldClassApp/target/*.jar /WCApp/

RUN ls /WCApp

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/WCApp/WorldClassBackEnd-0.0.1-SNAPSHOT.jar"]
