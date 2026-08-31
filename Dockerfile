# ==========================================
# Etapa 1: Build y compilación con Gradle
# ==========================================
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copia los archivos del wrapper de Gradle para aprovechar la caché
COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle

# Descarga de dependencias
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# Copia del código fuente y compilación del JAR (sin ejecutar tests en build)
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# ==========================================
# Etapa 2: Imagen ligera de ejecución (JRE)
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Usuario no root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia del artefacto compilado
COPY --from=builder /app/build/libs/*.jar app.jar

# Variables de entorno por defecto
ENV SERVER_PORT=9090
EXPOSE 9090

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]