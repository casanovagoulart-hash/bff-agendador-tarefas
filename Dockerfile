FROM maven:3.9.9-eclipse-temurin-17 AS maven_upstream
WORKDIR /app
COPY . .

# ARGUMENTO DIDÁTICO: O --mount=type=cache salva a pasta .m2 no cache do Docker.
# Se a rede oscilar ou você alterar uma linha de código Java, o Maven não precisa
# baixar todas as dependências da internet novamente.
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=maven_upstream /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
