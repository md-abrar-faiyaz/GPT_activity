# Use Java 17 JDK image
FROM eclipse-temurin:17-jdk

# Create a working directory
WORKDIR /app

# Copy the built jar into the container
COPY build/libs/presence-backend-1.0.0.jar app.jar

# Expose port 8080 for Render
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]