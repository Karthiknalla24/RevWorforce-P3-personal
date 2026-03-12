FROM openjdk:17-jdk-slim
WORKDIR /app
COPY services/notification-service/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
