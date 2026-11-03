FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/aula10_iqs-1.0.0.jar /app/app.jar
USER 10001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
