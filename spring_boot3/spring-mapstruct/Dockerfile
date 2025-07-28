# Use a smaller base image to run
FROM openjdk:21-jdk-slim
MAINTAINER Vaclav Demcak <vaclav.demcak@gmail.com>
# Set th working dir
WORKDIR /spring-mapstruct
# Copy the jar file from the previous stage
COPY target/*.jar spring-mapstruct-1.0-SNAPSHOT.jar
# Expose the application port
EXPOSE 8989

# Run the application
ENTRYPOINT ["java", "-jar", "spring-mapstruct-1.0-SNAPSHOT.jar"]