# ============================================
# MAKEFILE - Franchise Management API
# ============================================
# Automatización de Docker y Gradle para desarrollo

# Variables
DOCKER_COMPOSE = docker compose
APP_NAME = franchise-api
CONTAINER_APP = franchise-app
CONTAINER_DB = franchise-db
DB_NAME = franchise_accenture_db
BUILD_DATE = $(shell date -u +"%Y-%m-%dT%H:%M:%SZ")
VCS_REF = $(shell git rev-parse --short HEAD 2>/dev/null || echo "dev")

# Colores para mensajes
GREEN = \033[0;32m
YELLOW = \033[0;33m
RED = \033[0;31m
BLUE = \033[0;34m
CYAN = \033[0;36m
NC = \033[0m # No Color

# ============================================
# COMANDOS PRINCIPALES
# ============================================

.PHONY: help
help: ## Mostrar este mensaje de ayuda
	@echo "$(CYAN)=====================================================$(NC)"
	@echo "$(GREEN)  Accenture Franchise API - Comandos de Automatización$(NC)"
	@echo "$(CYAN)=====================================================$(NC)"
	@echo ""
	@echo "$(BLUE)Desarrollo Local:$(NC)"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | grep -v "Producción\|Helper\|Base de Datos\|Testing\|Limpieza\|Información" | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(CYAN)%-22s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(YELLOW)Base de Datos:$(NC)"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | grep "Base de Datos\|db-" | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(YELLOW)%-22s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(BLUE)Testing y Cobertura (JaCoCo):$(NC)"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | grep "test\|coverage" | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(BLUE)%-22s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(RED)Limpieza:$(NC)"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | grep "Limpieza\|clean" | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(RED)%-22s$(NC) %s\n", $$1, $$2}'
	@echo ""

# ============================================
# DESARROLLO LOCAL
# ============================================

.PHONY: run
run: ## Ejecutar la aplicación localmente con Docker (attached)
	@echo "$(GREEN)🚀 Levantando contenedores de la aplicación y base de datos...$(NC)"
	$(DOCKER_COMPOSE) up

.PHONY: run-build
run-build: ## Reconstruir imágenes y ejecutar en modo detached (-d)
	@echo "$(GREEN)🔨 Reconstruyendo imágenes y levantando en segundo plano...$(NC)"
	$(DOCKER_COMPOSE) up -d --build
	@echo "$(GREEN)✅ Contenedores listos y corriendo!$(NC)"
	@echo "$(CYAN)🌐 URL Base: http://localhost:9090/accenture/api/v1$(NC)"

.PHONY: stop
stop: ## Detener todos los contenedores
	@echo "$(YELLOW)🛑 Deteniendo contenedores...$(NC)"
	$(DOCKER_COMPOSE) down
	@echo "$(GREEN)✅ Contenedores detenidos$(NC)"

.PHONY: restart
restart: ## Reiniciar todos los contenedores
	@echo "$(YELLOW)🔄 Reiniciando contenedores...$(NC)"
	$(DOCKER_COMPOSE) restart
	@echo "$(GREEN)✅ Contenedores reiniciados$(NC)"

.PHONY: restart-app
restart-app: ## Reiniciar solo el contenedor de la API
	@echo "$(YELLOW)🔄 Reiniciando aplicación...$(NC)"
	$(DOCKER_COMPOSE) restart app
	@echo "$(GREEN)✅ Aplicación reiniciada$(NC)"

.PHONY: restart-db
restart-db: ## Reiniciar solo el contenedor de PostgreSQL
	@echo "$(YELLOW)🔄 Reiniciando base de datos...$(NC)"
	$(DOCKER_COMPOSE) restart postgres
	@echo "$(GREEN)✅ Base de datos reiniciada$(NC)"

# ============================================
# LOGS Y MONITOREO
# ============================================

.PHONY: logs
logs: ## Ver logs de todos los servicios en tiempo real
	$(DOCKER_COMPOSE) logs -f

.PHONY: logs-app
logs-app: ## Ver logs solo de la aplicación
	$(DOCKER_COMPOSE) logs -f app

.PHONY: logs-db
logs-db: ## Ver logs solo de PostgreSQL
	$(DOCKER_COMPOSE) logs -f postgres

.PHONY: ps
ps: ## Ver estado de los contenedores
	@echo "$(BLUE)📊 Estado de contenedores:$(NC)"
	@$(DOCKER_COMPOSE) ps

.PHONY: stats
stats: ## Ver uso de CPU y memoria de los contenedores
	@echo "$(BLUE)📈 Uso de recursos:$(NC)"
	@docker stats $(CONTAINER_APP) $(CONTAINER_DB)

# ============================================
# ACCESO A CONTENEDORES
# ============================================

.PHONY: shell-app
shell-app: ## Acceder al shell del contenedor de la aplicación
	@echo "$(BLUE)🐚 Accediendo al contenedor de la aplicación...$(NC)"
	@docker exec -it $(CONTAINER_APP) sh

.PHONY: shell-db
shell-db: ## Acceder a PostgreSQL (psql) interactivo
	@echo "$(BLUE)🐚 Accediendo a PostgreSQL (psql)...$(NC)"
	@docker exec -it $(CONTAINER_DB) psql -U postgres -d $(DB_NAME)

# ============================================
# BASE DE DATOS
# ============================================

.PHONY: db-backup
db-backup: ## Crear backup de la base de datos PostgreSQL
	@echo "$(GREEN)💾 Creando backup en carpeta backups/...$(NC)"
	@mkdir -p backups
	@docker exec -t $(CONTAINER_DB) pg_dump -U postgres $(DB_NAME) > backups/backup_$(shell date +%Y%m%d_%H%M%S).sql
	@echo "$(GREEN)✅ Backup creado exitosamente$(NC)"

.PHONY: db-restore
db-restore: ## Restaurar el último backup disponible
	@echo "$(YELLOW)📂 Restaurando último backup...$(NC)"
	@latest_backup=$$(ls -t backups/*.sql 2>/dev/null | head -1); \
	if [ -z "$$latest_backup" ]; then \
		echo "$(RED)❌ No se encontraron archivos de backup en backups/$(NC)"; \
		exit 1; \
	fi; \
	docker exec -i $(CONTAINER_DB) psql -U postgres $(DB_NAME) < $$latest_backup
	@echo "$(GREEN)✅ Backup restaurado exitosamente$(NC)"

.PHONY: db-reset
db-reset: ## Resetear la base de datos (⚠️ Elimina y recrea las tablas)
	@echo "$(RED)⚠️  ADVERTENCIA: Esto reiniciará todas las tablas de la BD$(NC)"
	@docker exec -i $(CONTAINER_DB) psql -U postgres -d $(DB_NAME) -c "TRUNCATE TABLE products, branches, franchises RESTART IDENTITY CASCADE;"
	@echo "$(GREEN)✅ Base de datos reseteada con éxito$(NC)"

# ============================================
# TESTING Y COBERTURA (JACOCO)
# ============================================

.PHONY: test
test: ## Ejecutar todos los tests unitarios con Gradle
	@echo "$(BLUE)🧪 Ejecutando suite de pruebas unitarias...$(NC)"
	./gradlew test
	@echo "$(GREEN)✅ Pruebas unitarias completadas$(NC)"

.PHONY: test-coverage
test-coverage: ## Ejecutar tests y generar reporte JaCoCo
	@echo "$(BLUE)🧪 Ejecutando tests y generando reporte de cobertura JaCoCo...$(NC)"
	./gradlew test jacocoTestReport
	@echo "$(GREEN)✅ Reporte generado en: build/reports/jacoco/index.html$(NC)"

.PHONY: open-coverage
open-coverage: ## Abrir el reporte HTML de JaCoCo en el navegador
	@echo "$(CYAN)🌐 Abriendo reporte de cobertura JaCoCo...$(NC)"
	@if command -v xdg-open > /dev/null 2>&1; then \
		xdg-open build/reports/jacoco/index.html; \
	elif command -v open > /dev/null 2>&1; then \
		open build/reports/jacoco/index.html; \
	elif command -v start > /dev/null 2>&1; then \
		start build/reports/jacoco/index.html; \
	else \
		echo "$(YELLOW)Abre manualmente: build/reports/jacoco/index.html$(NC)"; \
	fi

.PHONY: full-coverage
full-coverage: test-coverage open-coverage ## Ejecuta tests, genera reporte y lo abre en el navegador

# ============================================
# INFORMACIÓN Y UTILIDADES
# ============================================

.PHONY: info
info: ## Mostrar información del sistema y URLs
	@echo "$(CYAN)=====================================================$(NC)"
	@echo "$(GREEN)  Información del Microservicio$(NC)"
	@echo "$(CYAN)=====================================================$(NC)"
	@echo "$(YELLOW)📦 Proyecto:$(NC)       $(APP_NAME)"
	@echo "$(YELLOW)☕ Java:$(NC)           Java 21 (Temurin)"
	@echo "$(YELLOW)🚀 Stack:$(NC)          Spring Boot (WebFlux + R2DBC)"
	@echo "$(YELLOW)🌐 URL Base:$(NC)       http://localhost:9090/accenture/api/v1"
	@echo "$(YELLOW)🗄️  PostgreSQL:$(NC)     localhost:5432 (DB: $(DB_NAME))"
	@echo ""

.PHONY: endpoints
endpoints: ## Listar los endpoints principales de la API
	@echo "$(CYAN)=====================================================$(NC)"
	@echo "$(GREEN)  Endpoints Disponibles (REST API)$(NC)"
	@echo "$(CYAN)=====================================================$(NC)"
	@echo "$(YELLOW)Franquicias:$(NC)"
	@echo "  POST   /accenture/api/v1/franchises"
	@echo "  PATCH  /accenture/api/v1/franchises/{id}/name"
	@echo "  GET    /accenture/api/v1/franchises/{id}/max-stock-products"
	@echo "$(YELLOW)Sucursales:$(NC)"
	@echo "  POST   /accenture/api/v1/franchises/{franchiseId}/branches"
	@echo "  PATCH  /accenture/api/v1/branches/{id}/name"
	@echo "$(YELLOW)Productos:$(NC)"
	@echo "  POST   /accenture/api/v1/branches/{branchId}/products"
	@echo "  PATCH  /accenture/api/v1/products/{id}/stock"
	@echo "  PATCH  /accenture/api/v1/products/{id}/name"
	@echo "  DELETE /accenture/api/v1/branches/{branchId}/products/{productId}"
	@echo ""

# ============================================
# LIMPIEZA
# ============================================

.PHONY: clean
clean: ## Limpieza básica de Gradle y compilados
	@echo "$(YELLOW)🧹 Limpiando artefactos de compilación...$(NC)"
	./gradlew clean
	@echo "$(GREEN)✅ Limpieza completada$(NC)"

.PHONY: clean-all
clean-all: ## Detener contenedores y eliminar volúmenes e imágenes
	@echo "$(RED)⚠️  ADVERTENCIA: Se eliminarán todos los contenedores, volúmenes e imágenes$(NC)"
	$(DOCKER_COMPOSE) down -v --rmi all --remove-orphans
	@echo "$(GREEN)✅ Limpieza de Docker completa$(NC)"

# Target por defecto
.DEFAULT_GOAL := help