# ---------- build stage ----------
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace

# copy pom + download dependencies early (speeds up rebuilds)
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

# copy sources and build
COPY src ./src
RUN mvn -q -B package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre-jammy
ARG JAR_FILE=/workspace/target/phasezero-catalog-service-0.0.1-SNAPSHOT.jar
WORKDIR /app

# create non-root user
RUN useradd --create-home --shell /bin/bash appuser || true
USER appuser

# copy jar from build stage
COPY --from=build ${JAR_FILE} ./app.jar

# Expose service port
EXPOSE 8080

# simple healthcheck hitting /products (returns 200)
HEALTHCHECK --interval=15s --timeout=3s --retries=5 \
  CMD curl -f http://localhost:8080/products || exit 1

# runtime entry
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
