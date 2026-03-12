FROM openjdk:17-jdk-slim
WORKDIR /app
COPY services/reporting-service/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
