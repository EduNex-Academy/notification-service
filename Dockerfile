# Use official OpenJDK 21 image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew build.gradle settings.gradle /app/
RUN chmod +x gradlew
COPY gradle /app/gradle

# Copy the code
COPY src /app/src

# Build the application
RUN ./gradlew build --no-daemon -x test

# For easy debugs
RUN ls -la build/libs/

# Copy the built jar without getting any errors
RUN find build/libs -name "*.jar" -exec cp {} app.jar \;

# Expose port 8081
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
