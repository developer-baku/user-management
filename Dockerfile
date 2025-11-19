# ===== Stage 1: Build the application =====
FROM gradle:8.7-jdk21 AS build
WORKDIR /app

# Copy everything (Gradle needs settings + gradle dir)
COPY . .

# Build the JAR (skip tests)
RUN gradle bootJar --no-daemon -x test

# ===== Stage 2: Runtime image =====
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]