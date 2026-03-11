#!/bin/bash

# Script de teste JWT - API ControleGastos
# Execute: chmod +x test-jwt.sh && ./test-jwt.sh

set -e

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Configurações
API_URL="http://localhost:8080"
USER_EMAIL="teste.jwt@example.com"
USER_PASS="senha123"
USER_NAME="Teste JWT User"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Testando JWT Authentication${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Teste 1: Registrar usuário
echo -e "${GREEN}[1/6] Registrando novo usuário...${NC}"
REGISTER_RESPONSE=$(curl -s -X POST ${API_URL}/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d "{
    \"nome\": \"${USER_NAME}\",
    \"email\": \"${USER_EMAIL}\",
    \"senha\": \"${USER_PASS}\"
  }")

echo "$REGISTER_RESPONSE" | jq '.' 2>/dev/null || echo "$REGISTER_RESPONSE"
echo ""

# Teste 2: Fazer login e obter token
echo -e "${GREEN}[2/6] Fazendo login e obtendo token JWT...${NC}"
LOGIN_RESPONSE=$(curl -s -X POST ${API_URL}/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"${USER_EMAIL}\",
    \"senha\": \"${USER_PASS}\"
  }")

echo "$LOGIN_RESPONSE" | jq '.' 2>/dev/null || echo "$LOGIN_RESPONSE"

# Extrair token
if command -v jq &> /dev/null; then
    TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
    NOME=$(echo "$LOGIN_RESPONSE" | jq -r '.nome')
else
    # Fallback sem jq
    TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
    NOME=$USER_NAME
fi

if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
    echo -e "${RED}❌ Falha ao obter token. Verifique se a aplicação está rodando.${NC}"
    exit 1
fi

echo -e "\n${GREEN}✅ Login bem-sucedido!${NC}"
echo -e "${YELLOW}👤 Usuário: $NOME${NC}"
echo -e "${YELLOW}🎫 Token (primeiros 50 chars): ${TOKEN:0:50}...${NC}\n"

# Teste 3: Validar token
echo -e "${GREEN}[3/6] Validando token...${NC}"
VALIDATE_RESPONSE=$(curl -s -X GET ${API_URL}/api/auth/validate \
  -H "Authorization: Bearer $TOKEN")

echo "$VALIDATE_RESPONSE"
echo ""

# Teste 4: Listar usuários (autenticado)
echo -e "${GREEN}[4/6] Listando usuários (autenticado com JWT)...${NC}"
curl -s -H "Authorization: Bearer $TOKEN" \
  ${API_URL}/api/usuarios | jq '.' 2>/dev/null || \
  curl -s -H "Authorization: Bearer $TOKEN" ${API_URL}/api/usuarios
echo ""

# Teste 5: Listar gastos
echo -e "${GREEN}[5/6] Listando gastos...${NC}"
GASTOS=$(curl -s -H "Authorization: Bearer $TOKEN" ${API_URL}/api/gastos)
echo "$GASTOS" | jq '.' 2>/dev/null || echo "$GASTOS"
echo ""

# Teste 6: Calcular total
echo -e "${GREEN}[6/6] Calculando total de gastos...${NC}"
TOTAL=$(curl -s -H "Authorization: Bearer $TOKEN" ${API_URL}/api/gastos/total)
echo -e "${YELLOW}💰 Total: R$ $TOTAL${NC}\n"

# Testes adicionais
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Testes Adicionais${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Teste sem token (deve dar 401)
echo -e "${YELLOW}Testando acesso sem token (deve dar erro 401)...${NC}"
STATUS=$(curl -s -o /dev/null -w "%{http_code}" ${API_URL}/api/gastos)
if [ "$STATUS" = "401" ] || [ "$STATUS" = "403" ]; then
    echo -e "${GREEN}✅ Proteção funcionando! Status: $STATUS${NC}\n"
else
    echo -e "${RED}❌ Aviso: Endpoint não protegido! Status: $STATUS${NC}\n"
fi

# Teste com token inválido
echo -e "${YELLOW}Testando com token inválido (deve dar erro)...${NC}"
INVALID_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer token_invalido_123" \
  ${API_URL}/api/gastos)
if [ "$INVALID_STATUS" = "401" ] || [ "$INVALID_STATUS" = "403" ]; then
    echo -e "${GREEN}✅ Validação de token funcionando! Status: $INVALID_STATUS${NC}\n"
else
    echo -e "${RED}❌ Aviso: Token inválido aceito! Status: $INVALID_STATUS${NC}\n"
fi

# Resumo final
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  ✓ Testes JWT Concluídos!${NC}"
echo -e "${GREEN}========================================${NC}\n"

echo "📝 Resumo:"
echo "- Email: ${USER_EMAIL}"
echo "- Token válido por: 24 horas"
echo "- Total de gastos: R$ ${TOTAL}"
echo ""
echo "💡 Para usar o token em seus testes:"
echo "   export TOKEN=\"$TOKEN\""
echo "   curl -H \"Authorization: Bearer \$TOKEN\" ${API_URL}/api/gastos"
echo ""
echo "📚 Veja mais exemplos em: JWT_LOGIN_GUIDE.md"

