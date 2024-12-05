Here's a Dockerfile for a Spring MVC application named "spring-mvc-app":

```dockerfile
# Use a base image with Java and Maven installed
FROM maven:3.6.3-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files (pom.xml and src directory) to the container
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package

# Create a new container with only the JRE
FROM openjdk:17-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file from the 'build' container to this container
COPY --from=build /app/target/spring-mvc-app.jar ./app.jar

# Expose the port the application will run on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]
```

This Dockerfile is specifically tailored for a Spring MVC application named "spring-mvc-app". It uses a multi-stage build process to first compile the application with Maven and then create a lightweight runtime container with just the JRE and the compiled application.

The main differences from the Spring Boot example you provided are:

1. The application name is set to "spring-mvc-app" in the COPY command for the JAR file.
2. The base images and overall structure remain the same, as they are suitable for both Spring Boot and Spring MVC applications.

Make sure that your `pom.xml` file is configured correctly for your Spring MVC application and that the resulting JAR file is named `spring-mvc-app.jar`. If the JAR file has a different name, adjust the COPY command in the second stage of the Dockerfile accordingly.