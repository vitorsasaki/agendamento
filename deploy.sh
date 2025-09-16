#!/bin/bash

echo "🚀 Iniciando deploy do Agendamento na VPS..."

# Verificar se arquivo .env existe
if [ ! -f .env ]; then
    echo "❌ Arquivo .env não encontrado. Crie baseado no .env.example"
    exit 1
fi

# Parar containers existentes
echo "🛑 Parando containers existentes..."
docker-compose -f docker-compose.prod.yml down || true

# Fazer backup do banco (se existir)
echo "💾 Fazendo backup do banco de dados..."
docker exec agendamento-postgres-prod pg_dump -U postgres agendamento_prod > backup_$(date +%Y%m%d_%H%M%S).sql 2>/dev/null || echo "Nenhum backup necessário (primeira execução)"

# Build da nova imagem
echo "🔨 Fazendo build da aplicação..."
docker build -t agendamento:latest .

# Subir containers
echo "▶️ Iniciando containers..."
docker-compose -f docker-compose.prod.yml up -d

# Aguardar aplicação ficar pronta
echo "⏳ Aguardando aplicação ficar pronta..."
timeout 120 bash -c 'until curl -f http://agendamento.conect365.com/actuator/health; do sleep 5; done'

if [ $? -eq 0 ]; then
    echo "✅ Deploy concluído com sucesso!"
    echo "🌐 Aplicação disponível em: https://agendamento.conect365.com"
else
    echo "❌ Falha no deploy. Verificar logs:"
    docker-compose -f docker-compose.prod.yml logs --tail=50 app
fi