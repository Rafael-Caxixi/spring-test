#!/bin/bash

# Remove o .env antigo, se existir
if [ -f .env ]; then
  echo "Arquivo .env antigo encontrado, removendo..."
  rm .env
  echo "Arquivo .env antigo removido."
fi

# Variáveis fixas não editáveis
POSTGRES_DB="spring_junit_db"
POSTGRES_PORT="5432"
POSTGRES_HOST="db"
SERVER_PORT="8080"
JWT_SECRET_KEY="minhaChaveUltraSecretaComMaisDe32Bytes"
JWT_EXPIRATION_TIME="3600000"

# Função para ler e validar input não vazio
function ler_input_nao_vazio() {
  local prompt_msg=$1
  local valor=""
  while true; do
    read -p "$prompt_msg" valor
    if [[ -z "$valor" ]]; then
      echo "Valor não pode ser vazio. Tente novamente."
    else
      echo "$valor"
      break
    fi
  done
}

# Pergunta para o usuário, com validação
echo "Digite as credenciais do Postgres."

# POSTGRES_USER - permite padrão ou valor válido não vazio
read -p "Digite o POSTGRES_USER (padrão: postgres): " input_user
if [[ -z "$input_user" ]]; then
  POSTGRES_USER="postgres"
else
  POSTGRES_USER="$input_user"
fi

# POSTGRES_PASSWORD - obrigatoriamente não vazio, pede até digitar correto
POSTGRES_PASSWORD=$(ler_input_nao_vazio "Digite o POSTGRES_PASSWORD (obrigatório): ")

# Cria o arquivo .env com as variáveis fixas e as variáveis do usuário
cat > .env <<EOL
POSTGRES_DB=$POSTGRES_DB
POSTGRES_USER=$POSTGRES_USER
POSTGRES_PASSWORD=$POSTGRES_PASSWORD
POSTGRES_PORT=$POSTGRES_PORT
POSTGRES_HOST=$POSTGRES_HOST
SERVER_PORT=$SERVER_PORT
JWT_SECRET_KEY="$JWT_SECRET_KEY"
JWT_EXPIRATION_TIME=$JWT_EXPIRATION_TIME
EOL

echo ".env criado do zero com sucesso!"

# Mostra o conteúdo do arquivo .env
echo "Conteúdo do arquivo .env:"
cat .env

# Espera o usuário pressionar ENTER para finalizar
read -p "Pressione ENTER para finalizar..."
