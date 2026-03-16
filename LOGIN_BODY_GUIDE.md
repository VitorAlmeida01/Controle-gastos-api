# 🔐 Como Fazer Login na API

## 📋 Body do Request

Para fazer login, você deve enviar um JSON no body com os seguintes campos:

```json
{
  "email": "seu-email@example.com",
  "senha": "sua-senha"
}
```

### ✅ Campos Obrigatórios

| Campo | Tipo | Validação | Descrição |
|-------|------|-----------|-----------|
| `email` | String | Email válido | Email do usuário cadastrado |
| `senha` | String | Não vazio | Senha do usuário |

---

## 🚀 Exemplo de Login (cURL)

### Endpoint
```
POST http://localhost:8080/api/auth/login
```

### Login como ADMIN
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "senha": "admin123"
  }'
```

### Login como USER
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "senha123"
  }'
```

---

## 📤 Resposta de Sucesso (200 OK)

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJub21lIjoiQWRtaW4iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sImVtYWlsIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImlhdCI6MTczMzI2NTQ0NywiZXhwIjoxNzMzMzUxODQ3fQ.signature",
  "email": "admin@example.com",
  "nome": "Admin"
}
```

### 🔍 Estrutura da Resposta

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `token` | String | Token JWT para autenticação nas próximas requisições |
| `email` | String | Email do usuário |
| `nome` | String | Nome do usuário |

---

## 🎯 Informações Dentro do Token JWT

O token JWT agora contém as seguintes informações nas **claims**:

```json
{
  "nome": "Admin",
  "email": "admin@example.com",
  "roles": ["ROLE_ADMIN", "ROLE_USER"],
  "sub": "admin@example.com",
  "iat": 1733265447,
  "exp": 1733351847
}
```

### 📊 Claims do Token

| Claim | Descrição |
|-------|-----------|
| `nome` | Nome completo do usuário |
| `email` | Email do usuário |
| `roles` | Lista de roles/papéis do usuário |
| `sub` | Subject (email do usuário) |
| `iat` | Issued At (quando foi criado) |
| `exp` | Expiration (quando expira - 24h) |

---

## 🔓 Extrair Informações do Token

### Endpoint `/api/auth/me`

Para extrair as informações do token sem precisar decodificá-lo manualmente:

```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
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

## 👥 Usuários Padrão Cadastrados

### 1. Administrador
```json
{
  "email": "admin@example.com",
  "senha": "admin123",
  "roles": ["ROLE_ADMIN", "ROLE_USER"]
}
```

### 2. Usuário Comum
```json
{
  "email": "joao@example.com",
  "senha": "senha123",
  "roles": ["ROLE_USER"]
}
```

---

## 🔒 Como Usar o Token nas Requisições

Após fazer login, use o token recebido nas próximas requisições:

```bash
# Salvar o token em uma variável
TOKEN="eyJhbGciOiJIUzUxMiJ9..."

# Usar o token em uma requisição
curl -X GET http://localhost:8080/api/gastos \
  -H "Authorization: Bearer $TOKEN"
```

### Com jq (para extrair o token automaticamente)
```bash
# Fazer login e salvar o token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  | jq -r '.token')

# Usar o token
curl -X GET http://localhost:8080/api/gastos \
  -H "Authorization: Bearer $TOKEN"

# Ver informações do token
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN"
```

---

## ❌ Erros Possíveis

### 401 Unauthorized - Credenciais Inválidas
```json
"Email ou senha inválidos"
```
**Causa:** Email ou senha incorretos

### 400 Bad Request - Validação
```json
{
  "timestamp": "2026-03-14T...",
  "status": 400,
  "error": "Bad Request",
  "message": "Email é obrigatório"
}
```
**Causa:** Campo obrigatório não enviado ou email inválido

### 500 Internal Server Error
```json
"Erro ao realizar login: [mensagem de erro]"
```
**Causa:** Erro interno do servidor

---

## 📱 Exemplo com Postman/Insomnia

### 1. Configurar a Requisição
- **Método:** POST
- **URL:** `http://localhost:8080/api/auth/login`
- **Headers:**
  - `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "email": "admin@example.com",
  "senha": "admin123"
}
```

### 2. Salvar o Token
Após receber a resposta, copie o campo `token` da resposta.

### 3. Usar o Token
Em requisições subsequentes:
- **Headers:**
  - `Authorization: Bearer SEU_TOKEN_COPIADO`

---

## 🧪 Testar Roles

### Como ADMIN (pode fazer tudo)
```bash
# Login como admin
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  | jq -r '.token')

# Ver suas roles
curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

### Como USER (acesso limitado)
```bash
# Login como user
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@example.com","senha":"senha123"}' \
  | jq -r '.token')

# Ver suas roles
curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

---

## 🎓 Resumo Rápido

### Para fazer login você precisa enviar:
```json
{
  "email": "admin@example.com",
  "senha": "admin123"
}
```

### Você vai receber:
```json
{
  "token": "JWT_TOKEN_AQUI",
  "email": "admin@example.com",
  "nome": "Admin"
}
```

### O token contém (agora com roles!):
- ✅ Nome do usuário
- ✅ Email do usuário
- ✅ Roles do usuário (ROLE_ADMIN, ROLE_USER, etc.)
- ✅ Data de expiração (24 horas)

### Use o token assim:
```
Authorization: Bearer SEU_TOKEN
```

---

## 📚 Documentos Relacionados

- [JWT Login Guide](./JWT_LOGIN_GUIDE.md) - Guia completo de JWT
- [Roles Explanation](./ROLES_EXPLANATION.md) - Sistema de roles
- [cURL Commands](./CURL_COMMANDS.md) - Todos os comandos da API

