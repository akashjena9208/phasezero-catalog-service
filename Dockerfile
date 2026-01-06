# -------- Build Stage --------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom first to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build jar
COPY src ./src
RUN mvn clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the final jar from build stage
COPY --from=build /app/target/phasezerocatalogservice.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

