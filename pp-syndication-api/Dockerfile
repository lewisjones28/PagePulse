# Use an official Java runtime as a parent image
FROM openjdk:25-ea-21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application's JAR file into the container
COPY ./target/*.jar /app/app.jar

# Expose the port the application runs on
EXPOSE 8089

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
