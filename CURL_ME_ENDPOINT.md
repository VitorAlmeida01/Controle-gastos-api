# 🔐 cURL para Endpoint /api/auth/me

## 📋 Descrição

O endpoint `/api/auth/me` extrai e retorna as informações do usuário que estão armazenadas no token JWT, sem precisar consultar o banco de dados.

---

## 🚀 cURL Básico

```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer SEU_TOKEN_JWT_AQUI"
```

---

## 📝 Exemplo Completo (Passo a Passo)

### 1️⃣ Fazer Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "senha": "admin123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJub21lIjoiQWRtaW4iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sImVtYWlsIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImlhdCI6MTczMzI2NTQ0NywiZXhwIjoxNzMzMzUxODQ3fQ.signature",
  "email": "admin@example.com",
  "nome": "Admin"
}
```

### 2️⃣ Copiar o Token

Copie o valor do campo `token` da resposta.

### 3️⃣ Chamar /me

```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJub21lIjoiQWRtaW4i..."
```

**Resposta:**
```json
{
  "email": "admin@example.com",
  "nome": "Admin",
  "roles": ["ROLE_ADMIN", "ROLE_USER"]
}
```

---

## 🎯 Exemplo com jq (Automático)

### Login + /me em um comando

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  | jq -r '.token') && \
echo "Token: ${TOKEN:0:50}..." && \
curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Output:**
```
Token: eyJhbGciOiJIUzUxMiJ9.eyJub21lIjoiQWRtaW4iLCJyb2xl...
{
  "email": "admin@example.com",
  "nome": "Admin",
  "roles": [
    "ROLE_ADMIN",
    "ROLE_USER"
  ]
}
```

---

## 👥 Exemplos com Diferentes Usuários

### Como ADMIN

```bash
# Login como ADMIN
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' | jq -r '.token')

# Ver informações
curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
{
  "email": "admin@example.com",
  "nome": "Admin",
  "roles": ["ROLE_ADMIN", "ROLE_USER"]
}
```

---

### Como USER

```bash
# Login como USER
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@example.com","senha":"senha123"}' | jq -r '.token')

# Ver informações
curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
{
  "email": "joao@example.com",
  "nome": "João Silva",
  "roles": ["ROLE_USER"]
}
```

---

## 🔄 Script Completo para Testes

```bash
#!/bin/bash

echo "==================================="
echo "   TESTE DO ENDPOINT /api/auth/me"
echo "==================================="
echo ""

# Teste com ADMIN
echo "1. Login como ADMIN..."
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' | jq -r '.token')

echo "   Token: ${ADMIN_TOKEN:0:50}..."
echo ""

echo "2. Consultando /me como ADMIN..."
curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.'
echo ""

# Teste com USER
echo "3. Login como USER..."
USER_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@example.com","senha":"senha123"}' | jq -r '.token')

echo "   Token: ${USER_TOKEN:0:50}..."
echo ""

echo "4. Consultando /me como USER..."
curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $USER_TOKEN" | jq '.'
echo ""

echo "==================================="
echo "   TESTES CONCLUÍDOS!"
echo "==================================="
```

---

## 📱 Postman/Insomnia

### Configuração

1. **Método:** `GET`
2. **URL:** `http://localhost:8080/api/auth/me`
3. **Headers:**
   - `Authorization: Bearer SEU_TOKEN_AQUI`

### Passos

1. Primeiro faça login em `/api/auth/login`
2. Copie o `token` da resposta
3. Vá para a requisição `/api/auth/me`
4. Na aba **Headers**, adicione:
   - Key: `Authorization`
   - Value: `Bearer [cole_o_token_aqui]`
5. Clique em **Send**

---

## ❌ Erros Comuns

### 401 - "Token não fornecido"

```json
"Token não fornecido"
```

**Causa:** Falta o header `Authorization` ou não começa com "Bearer "

**Solução:**
```bash
# ❌ ERRADO
curl -X GET http://localhost:8080/api/auth/me

# ❌ ERRADO
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: eyJhbGc..."

# ✅ CORRETO
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer eyJhbGc..."
```

---

### 401 - "Erro ao extrair informações do token"

```json
"Erro ao extrair informações do token: [mensagem]"
```

**Causa:** Token inválido, malformado ou expirado (24h)

**Solução:** Faça login novamente para obter um novo token

---

## 🔍 Diferença entre /validate e /me

### `/api/auth/validate`
- ✅ Valida se o token está válido
- ✅ Responde apenas "Token válido" ou erro
- ✅ Verifica contra o banco de dados

### `/api/auth/me`
- ✅ Extrai e retorna as informações do token
- ✅ Responde com email, nome e roles
- ✅ Não consulta o banco de dados (mais rápido)

---

## 📊 Resposta Completa

```json
{
  "email": "admin@example.com",
  "nome": "Admin",
  "roles": [
    "ROLE_ADMIN",
    "ROLE_USER"
  ]
}
```

### Campos da Resposta

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `email` | String | Email do usuário (extraído do token) |
| `nome` | String | Nome completo do usuário (extraído do token) |
| `roles` | Array | Lista de roles/papéis do usuário |

---

## 🎯 Use Cases

### 1. Carregar informações do usuário no frontend

```javascript
// JavaScript/TypeScript
const token = localStorage.getItem('token');

fetch('http://localhost:8080/api/auth/me', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
.then(res => res.json())
.then(data => {
  console.log('User:', data.nome);
  console.log('Email:', data.email);
  console.log('Roles:', data.roles);
});
```

### 2. Verificar se o usuário é admin

```bash
TOKEN="seu_token"
ROLES=$(curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" | jq -r '.roles[]')

if echo "$ROLES" | grep -q "ROLE_ADMIN"; then
  echo "Usuário é ADMIN"
else
  echo "Usuário NÃO é ADMIN"
fi
```

### 3. Exibir saudação personalizada

```bash
TOKEN="seu_token"
NOME=$(curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" | jq -r '.nome')

echo "Bem-vindo, $NOME!"
```

---

## 📚 Documentos Relacionados

- [Login Body Guide](./LOGIN_BODY_GUIDE.md) - Como fazer login
- [JWT Login Guide](./JWT_LOGIN_GUIDE.md) - Guia completo JWT
- [Roles Explanation](./ROLES_EXPLANATION.md) - Sistema de roles
- [cURL Commands](./CURL_COMMANDS.md) - Todos os comandos

