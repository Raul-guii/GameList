# Estágio 1: Build (Compilação)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copia e baixa as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e empacota o JAR
COPY src ./src
RUN mvn package -DskipTests

# --- Estágio 2: Runtime (Execução) ---
FROM eclipse-temurin:21-jre-alpine

# Nome do arquivo JAR que será copiado
ARG JAR_FILE_NAME=gamelist-0.0.1-SNAPSHOT.jar

# Copia o JAR do estágio de build para o diretório de trabalho
COPY --from=build /app/target/${JAR_FILE_NAME} app.jar

# Expõe a porta
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]