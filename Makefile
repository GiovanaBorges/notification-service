# Carrega variáveis do .env
-include .env.secrets
export

IMAGE_FULL=$(DOCKER_USER)/$(IMAGE_NAME):$(TAG)
ENV ?= dev

.PHONY: up build login push

# 🔥 Pipeline principal (sem secrets)
up: build login push secret
	@echo "🚀 Tudo pronto! App e Docker configurados."

# 🐳 Build da imagem Docker
build:
	docker build -t $(IMAGE_FULL) .

# 🔐 Login no Docker Hub
login:
	echo $(DOCKER_PASSWORD) | docker login -u $(DOCKER_USER) --password-stdin

# 📤 Push da imagem
push:
	docker push $(IMAGE_FULL)

# 🔑 Criar GitHub Secrets (manual)
secret:
	@echo "🔑 Criando GitHub Secrets para ENV=$(ENV)..."
	@chmod +x create_secrets_auto.sh
	@./create_secrets_auto.sh $(ENV)