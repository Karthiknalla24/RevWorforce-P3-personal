FROM openjdk:17-jdk-slim
WORKDIR /app
COPY infrastructure/eureka-server/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
