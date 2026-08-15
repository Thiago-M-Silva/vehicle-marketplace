# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

# The Maven base image sets MAVEN_CONFIG=/root/.m2. Maven Wrapper treats this
# variable as command-line arguments, which makes Maven interpret it as a
# lifecycle phase. The local repository keeps using Maven's default location.
ENV MAVEN_CONFIG=""

# Maven wrapper
COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw

# Source code
COPY src src

RUN ./mvnw package


# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /work

# Copy Quarkus fast-jar structure
COPY --from=build /workspace/target/quarkus-app/lib/ /work/lib/
COPY --from=build /workspace/target/quarkus-app/*.jar /work/
COPY --from=build /workspace/target/quarkus-app/app/ /work/app/
COPY --from=build /workspace/target/quarkus-app/quarkus/ /work/quarkus/

# Create non-root user
RUN groupadd --system quarkus \
    && useradd --system --gid quarkus quarkus \
    && chown -R quarkus:quarkus /work

USER quarkus

EXPOSE 8080

# Quarkus configuration
ENV QUARKUS_HTTP_HOST=0.0.0.0

ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]
