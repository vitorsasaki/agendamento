# ==========================================
# ESTÁGIO 1: BUILD DA APLICAÇÃO
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven primeiro (para cache de layers)
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Dar permissão de execução para o mvnw (importante no Linux)
RUN chmod +x mvnw

# Baixar dependências (isso fica em cache se o pom.xml não mudar)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src src

# Fazer build da aplicação (pular testes por enquanto)
RUN ./mvnw clean package -DskipTests

# ==========================================
# ESTÁGIO 2: RUNTIME DA APLICAÇÃO
# ==========================================
FROM openjdk:21-jdk-slim

# Definir diretório de trabalho
WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copiar o JAR da aplicação do estágio de build
COPY --from=build /app/target/agendamento-*.jar agendamento.jar

# Mudar para usuário não-root
USER spring:spring

# Expor a porta da aplicação
EXPOSE 8080

# Configurações da JVM para otimização
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport"

# Health check do container
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} -jar agendamento.jar"]