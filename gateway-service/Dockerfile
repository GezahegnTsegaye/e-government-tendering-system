FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

# Copy maven executable and pom.xml files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY gateway-service/pom.xml gateway-service/

# Build all dependencies
RUN ./mvnw dependency:go-offline -B -pl gateway-service

# Copy the project source
COPY gateway-service/src gateway-service/src

# Package the application
RUN ./mvnw package -B -DskipTests -pl gateway-service

# Create a lightweight image with only the JRE and the application
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp

# Add a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the application from the build stage
COPY --from=build /workspace/app/gateway-service/target/*.jar app.jar

# Set environment variables and expose port
ENV JAVA_OPTS=""
EXPOSE 8080

# Run the application with proper command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]