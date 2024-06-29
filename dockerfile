# Start with a base image containing Java runtime (JDK 17 in this case)
FROM openjdk:17-jdk-slim

# Add Maintainer Info
LABEL maintainer="jlthompson96"

# Set the current working directory in the image
WORKDIR /app

# Copy gradle executable to the image
COPY gradlew .
COPY gradle gradle

# Grant permission to execute the gradlew script
RUN chmod +x ./gradlew

# Copy the rest of the application to the image
COPY . .

# Build the application using Gradle
RUN ./gradlew build

# Change the working directory in the image to the build output directory
WORKDIR /app/build/libs

# Run the application
CMD ["java", "-jar", "bullrun-0.0.1-SNAPSHOT.jar"]