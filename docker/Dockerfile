# Use an official Java 8 runtime as a parent image
FROM openjdk:8-jre-alpine

# Copy the application JAR file to the container
COPY cloudservice-0.0.1-SNAPSHOT.jar /cloudservice-0.0.1-SNAPSHOT.jar

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "cloudservice-0.0.1-SNAPSHOT.jar"]
