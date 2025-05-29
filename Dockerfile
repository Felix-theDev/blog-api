FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy source code
COPY . .

# Install Maven
RUN apk add --no-cache maven

# Build the project
RUN mvn clean package -DskipTests

# Use built JAR from target
CMD ["java", "-jar", "target/blog-0.0.1-SNAPSHOT.jar"]
