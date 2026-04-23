# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY . .
RUN mvn clean install

# Run stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /build/auth-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]