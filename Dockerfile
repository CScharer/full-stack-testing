# CJS QA Automation Framework - Dockerfile
# Multi-stage build for efficient test execution

# Stage 1: Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build

LABEL maintainer="CJS Consulting, L.L.C"
LABEL description="Selenium Test Automation Framework with Cucumber BDD"
LABEL version="1.0.0"

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better caching)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies (this layer will be cached)
# Note: dependency:go-offline may fail for some plugins, continue anyway
RUN ./mvnw dependency:go-offline -B || echo "Some dependencies could not be resolved offline"

# Copy source code
COPY src ./src
COPY Configurations ./Configurations
COPY Data ./Data
COPY XML ./XML

# Copy configuration files
COPY log4j.properties log4j.xml ./
COPY checkstyle-custom.xml checkstyle-suppressions.xml ./

# Build the project (skip tests in build stage)
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jdk

LABEL maintainer="CJS Consulting, L.L.C"

# Install required utilities (using apt for Debian-based image)
RUN apt-get update && apt-get install -y \
    curl \
    bash \
    tzdata \
    && rm -rf /var/lib/apt/lists/*

# Set timezone
ENV TZ=America/Chicago
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Create app user (don't run as root)
# Use existing group 1000 or create if needed
RUN groupadd -g 1001 appuser || true && \
    useradd -r -u 1001 -g 1001 -m -d /home/appuser appuser || true

# Set working directory
WORKDIR /app

# Copy built artifacts from build stage
COPY --from=build /app/target ./target
COPY --from=build /app/src ./src
COPY --from=build /app/Configurations ./Configurations
COPY --from=build /app/Data ./Data
COPY --from=build /app/XML ./XML
COPY --from=build /app/log4j.properties /app/log4j.xml ./
COPY --from=build /app/pom.xml ./

# Copy Maven wrapper for test execution
COPY --from=build /app/.mvn ./.mvn
COPY --from=build /app/mvnw ./

# Create directories for test results
RUN mkdir -p target/surefire-reports target/cucumber-reports && \
    chown -R appuser:appuser /app

# Switch to app user
USER appuser

# Environment variables for test execution
ENV SELENIUM_REMOTE_URL=http://selenium-hub:4444/wd/hub
ENV BROWSER=chrome
ENV HEADLESS=false
ENV PARALLEL_THREADS=5

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:4444/wd/hub/status || exit 1

# Default command: Run all tests
ENTRYPOINT ["./mvnw", "test"]

# Override with specific test if needed
# docker run --rm cjs-qa-tests -Dtest=Scenarios#Google
