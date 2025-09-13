#!/bin/bash

echo "🔧 Configurando VPS para o Agendamento..."

# Atualizar sistema
sudo apt update && sudo apt upgrade -y

# Instalar Docker
echo "🐳 Instalando Docker..."
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Instalar Docker Compose
echo "🐙 Instalando Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Instalar Git
sudo apt install git -y

# Criar diretório da aplicação
mkdir -p /opt/agendamento
cd /opt/agendamento

echo "✅ VPS configurada! Próximos passos:"
echo "1. Clone seu repositório: git clone <seu-repo>"
echo "2. Configure o arquivo .env"
echo "3. Execute: ./deploy.sh"