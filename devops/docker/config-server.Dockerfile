# Config Server Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY infrastructure/config-server/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
