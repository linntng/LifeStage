# Use a lightweight OpenJDK 21 base image
FROM eclipse-temurin:21-jdk
# Set the working directory inside the container
WORKDIR /app

RUN mvn install
# Copy the built JAR file into the container
COPY target/eureka-0.0.1-SNAPSHOT.jar app.jar
# Expose the Spring Boot port
EXPOSE 9099
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]