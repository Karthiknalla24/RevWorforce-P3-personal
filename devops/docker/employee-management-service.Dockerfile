FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY services/employee-management-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
