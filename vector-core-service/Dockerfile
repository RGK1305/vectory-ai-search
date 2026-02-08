# Stage 1: Build the application
# We use the official Maven image based on Eclipse Temurin
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the JAR file, skipping tests to save time
RUN mvn clean package -DskipTests

# Stage 2: Run the application
# We use the JRE (Runtime) image which is much smaller (~150MB)
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the built JAR from the previous stage (using wildcard * to handle versions)
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]