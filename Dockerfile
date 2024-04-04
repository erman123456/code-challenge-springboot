FROM ubuntu:latest
LABEL authors="ermanjaks"

ENTRYPOINT ["top", "-b"]
# Use an official Maven image as a base image
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the project's pom.xml file to the container
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline --fail-never

# Copy the application source code to the container
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Create a new stage for the runtime image
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file from the build stage to the container
COPY --from=build /app/target/*.jar ./app.jar

# Specify the command to run your application
CMD ["java", "-jar", "app.jar"]
