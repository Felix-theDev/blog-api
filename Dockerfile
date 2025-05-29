# Use official OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built jar
COPY target/*.jar app.jar

#Expose the applcation port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
