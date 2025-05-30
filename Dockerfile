FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app


RUN apk add --no-cache maven

COPY . .


RUN mvn clean package -DskipTests


EXPOSE 8080

# Limit JVM heap size to prevent memory overuse on Railway's 500MB limit
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "target/blog-0.0.1-SNAPSHOT.jar"]
