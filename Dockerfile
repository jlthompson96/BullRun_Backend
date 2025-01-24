# Use a base image compatible with ARM (e.g., Azul Zulu OpenJDK)
FROM azul/zulu-openjdk-alpine:21-latest

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from your local system to the container
COPY build/libs/bullrun-1.0.0-RELEASE.jar app.jar

# Expose the port your Spring Boot app listens on
EXPOSE 8080

# Command to run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]

# Use build arguments
ARG TWELVE_DATA_API_KEY
ARG POLYGON_API_KEY
ARG MONGODB_URI
ARG MONGODB_DATABASE

# Set as environment variables in the image
ENV TWELVE_DATA_API_KEY=${TWELVE_DATA_API_KEY}
ENV POLYGON_API_KEY=${POLYGON_API_KEY}
ENV MONGODB_URI=${MONGODB_URI}
ENV MONGODB_DATABASE=${MONGODB_DATABASE}


# Health check to monitor the application
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1