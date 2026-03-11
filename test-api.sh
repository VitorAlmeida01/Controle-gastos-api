#!/bin/bash

# Script de teste completo da API ControleGastos
# Execute: chmod +x test-api.sh && ./test-api.sh

set -e  # Para em caso de erro

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configurações
API_URL="http://localhost:8080"
USER_EMAIL="teste@example.com"
USER_PASS="senha123"
USER_NAME="Teste Usuario"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Testando API ControleGastos${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Teste 1: Registrar usuário
echo -e "${GREEN}[1/8] Registrando novo usuário...${NC}"
REGISTER_RESPONSE=$(curl -s -X POST ${API_URL}/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d "{
    \"nome\": \"${USER_NAME}\",
    \"email\": \"${USER_EMAIL}\",
    \"senha\": \"${USER_PASS}\"
  }")

echo "Resposta: ${REGISTER_RESPONSE}"
USER_ID=$(echo ${REGISTER_RESPONSE} | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "User ID: ${USER_ID}\n"

# Teste 2: Listar usuários
echo -e "${GREEN}[2/8] Listando usuários (com autenticação)...${NC}"
curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/usuarios | jq '.' || echo "jq não instalado, mostrando raw:"
curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/usuarios
echo -e "\n"

# Teste 3: Verificar acesso ao H2 Console
echo -e "${GREEN}[3/8] Verificando H2 Console...${NC}"
H2_STATUS=$(curl -s -o /dev/null -w "%{http_code}" ${API_URL}/h2-console)
if [ ${H2_STATUS} -eq 200 ] || [ ${H2_STATUS} -eq 302 ]; then
  echo -e "${GREEN}✓ H2 Console acessível${NC}\n"
else
  echo -e "${RED}✗ H2 Console não acessível (Status: ${H2_STATUS})${NC}\n"
fi

# Teste 4: Criar categoria (via SQL seria necessário, aqui simulamos)
echo -e "${GREEN}[4/8] Criando categoria...${NC}"
echo "⚠️  Categorias precisam ser criadas no banco de dados"
echo "Execute no H2 Console:"
echo "INSERT INTO CATEGORIA (ID, TIPO, DT_CRIACAO, USUARIO_ID) VALUES (RANDOM_UUID(), 'MERCADO', CURRENT_DATE, '${USER_ID}');"
echo -e "\n"

# Aguardar entrada do usuário
echo -e "${BLUE}Pressione ENTER após criar a categoria no H2 Console...${NC}"
read -r

# Solicitar ID da categoria criada
echo -e "${BLUE}Digite o UUID da categoria criada:${NC}"
read -r CATEGORIA_ID

# Teste 5: Criar gasto
echo -e "${GREEN}[5/8] Criando gasto...${NC}"
GASTO_RESPONSE=$(curl -s -X POST ${API_URL}/api/gastos \
  -u ${USER_EMAIL}:${USER_PASS} \
  -H "Content-Type: application/json" \
  -d "{
    \"valor\": 150.50,
    \"categoria\": {
      \"id\": \"${CATEGORIA_ID}\"
    }
  }")

echo "Resposta: ${GASTO_RESPONSE}"
GASTO_ID=$(echo ${GASTO_RESPONSE} | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "Gasto ID: ${GASTO_ID}\n"

# Teste 6: Listar gastos
echo -e "${GREEN}[6/8] Listando todos os gastos...${NC}"
curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/gastos | jq '.' || curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/gastos
echo -e "\n"

# Teste 7: Buscar gasto por ID
echo -e "${GREEN}[7/8] Buscando gasto por ID...${NC}"
curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/gastos/${GASTO_ID} | jq '.' || curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/gastos/${GASTO_ID}
echo -e "\n"

# Teste 8: Calcular total
echo -e "${GREEN}[8/8] Calculando total de gastos...${NC}"
TOTAL=$(curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/gastos/total)
echo -e "Total: R$ ${TOTAL}\n"

# Testes adicionais
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Testes Adicionais (Opcional)${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Atualizar gasto
echo -e "${GREEN}Atualizando gasto...${NC}"
curl -s -X PUT ${API_URL}/api/gastos/${GASTO_ID} \
  -u ${USER_EMAIL}:${USER_PASS} \
  -H "Content-Type: application/json" \
  -d "{
    \"valor\": 200.00,
    \"categoria\": {
      \"id\": \"${CATEGORIA_ID}\"
    }
  }" | jq '.' || echo "Atualizado"
echo -e "\n"

# Buscar por tipo
echo -e "${GREEN}Buscando gastos por tipo MERCADO...${NC}"
curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/gastos/tipo/MERCADO | jq '.' || curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/gastos/tipo/MERCADO
echo -e "\n"

# Calcular total por tipo
echo -e "${GREEN}Calculando total por tipo MERCADO...${NC}"
TOTAL_TIPO=$(curl -s -u ${USER_EMAIL}:${USER_PASS} ${API_URL}/api/gastos/total/tipo/MERCADO)
echo -e "Total MERCADO: R$ ${TOTAL_TIPO}\n"

# Deletar gasto (opcional)
echo -e "${BLUE}Deseja deletar o gasto criado? (s/N)${NC}"
read -r DELETE_CONFIRM
if [ "${DELETE_CONFIRM}" = "s" ] || [ "${DELETE_CONFIRM}" = "S" ]; then
  echo -e "${GREEN}Deletando gasto...${NC}"
  curl -s -X DELETE ${API_URL}/api/gastos/${GASTO_ID} \
    -u ${USER_EMAIL}:${USER_PASS}
  echo -e "${GREEN}✓ Gasto deletado${NC}\n"
fi

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  ✓ Testes concluídos!${NC}"
echo -e "${GREEN}========================================${NC}\n"

echo "Resumo:"
echo "- User ID: ${USER_ID}"
echo "- Categoria ID: ${CATEGORIA_ID}"
echo "- Gasto ID: ${GASTO_ID}"
echo "- Total: R$ ${TOTAL}"

