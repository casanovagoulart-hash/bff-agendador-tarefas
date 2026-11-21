FROM maven:3.9.9-eclipse-temurin-17 AS maven_upstream
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=maven_upstream /app/target/*.jar app.jar

# CORREÇÃO: Porta alinhada com o server.port=8083 e o docker-compose.yml
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
