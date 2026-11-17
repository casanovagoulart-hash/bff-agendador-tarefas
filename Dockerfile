# =====================================================================
# ETAPA 1: Construção da Aplicação (A Fase de Build / Compilação)
# =====================================================================
# Utiliza uma imagem oficial do Maven com Java 17 (Eclipse Temurin)
# para baixar as dependências e compilar o projeto. Nomeamos esta etapa como 'maven_upstream'.
FROM maven:3.9.9-eclipse-temurin-17 AS maven_upstream

# Define o diretório de trabalho padrão dentro do container para onde os comandos serão apontados.
WORKDIR /app

# Os dois pontos ( . . ). Copia todo o código-fonte do projeto local para dentro do diretório /app do container.
# O primeiro ponto representa a pasta atual na sua máquina (contexto de build),
# e o segundo ponto representa o destino atual dentro do container (/app).
COPY . .

# Executa o empacotamento da aplicação.
# A flag -DskipTests (frequentemente utilizada em comandos do Maven ou Gradle/Spring Boot)
# tem como principal objetivo pular a execução dos testes automatizados durante o processo de build,
# empacotamento ou instalação da aplicação.
RUN mvn clean install -DskipTests

# =====================================================================
# ETAPA 2: Imagem Final de Execução (A Fase de Execução / Runtime)
# =====================================================================
# Utiliza uma imagem leve (Alpine Linux com Java 17 JDK) apenas para rodar o .jar gerado.
# Isso reduz drasticamente o tamanho final da imagem no Docker Hub.
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho padrão para a aplicação final.
WORKDIR /app

# Copia o arquivo .jar compilado na etapa anterior para o diretório atual do container,
# renomeando-o para um nome limpo e padronizado (app.jar).
# CORREÇÃO: O alias da etapa anterior era 'maven_upstream', então o '--from' deve apontar para ele,
# e o caminho padrão do Maven para o artefato gerado é /app/target/*.jar.
COPY --from=maven_upstream /app/target/*.jar app.jar

# Expõe a porta onde o BFF vai escutar as requisições.
# CORREÇÃO: Alinhado para a porta 8080 (ou ajuste para 8084 caso sua aplicação use estritamente essa porta por configuração).
EXPOSE 8080

# Comando padrão executado automaticamente quando o container é iniciado,
# subindo a aplicação Spring Boot através do Java.
ENTRYPOINT ["java", "-jar", "app.jar"]
