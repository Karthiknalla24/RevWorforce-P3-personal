FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY services/leave-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
