# 🔐 Login JWT - Guia Rápido

## 🚀 Como usar JWT nesta API

Esta API implementa autenticação via **JWT (JSON Web Token)**. Após fazer login, você recebe um token que deve ser enviado em todas as requisições protegidas.

---

## ⚡ Comandos Essenciais

### 1. Registrar Novo Usuário
```bash
curl -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123"
  }'
```

### 2. Fazer Login (Obter Token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "senha123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA3MDY3MjAwLCJleHAiOjE3MDcxNTM2MDB9...",
  "tipo": "Bearer",
  "email": "joao@example.com",
  "nome": "João Silva"
}
```

### 3. Usar Token em Requisições
```bash
# Salvar token em variável
export TOKEN="cole_o_token_aqui"

# Usar em requisições
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/gastos
```

---

## 📋 Exemplo Completo (Copy & Paste)

```bash
#!/bin/bash

# Passo 1: Registrar
echo "📝 Registrando usuário..."
curl -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste User",
    "email": "teste@example.com",
    "senha": "senha123"
  }'

echo -e "\n"

# Passo 2: Login e salvar token
echo "🔐 Fazendo login..."
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@example.com",
    "senha": "senha123"
  }')

# Extrair token (requer jq)
TOKEN=$(echo $RESPONSE | jq -r '.token')
echo "✅ Token obtido: ${TOKEN:0:50}..."

# Passo 3: Validar token
echo -e "\n🔍 Validando token..."
curl -X GET http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer $TOKEN"

echo -e "\n"

# Passo 4: Listar gastos com token
echo "📊 Listando gastos..."
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/gastos

echo -e "\n"

# Passo 5: Ver total
echo "💰 Total de gastos:"
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/gastos/total
```

**Como usar:**
1. Copie o script acima
2. Salve como `test-jwt.sh`
3. Execute: `chmod +x test-jwt.sh && ./test-jwt.sh`

---

## 🎯 Endpoints de Autenticação

| Endpoint | Método | Auth? | Descrição |
|----------|--------|-------|-----------|
| `/api/usuarios/register` | POST | ❌ Não | Registrar novo usuário |
| `/api/auth/login` | POST | ❌ Não | Fazer login e obter token |
| `/api/auth/validate` | GET | ✅ Sim | Validar se token é válido |

---

## 🔑 Como Funciona JWT

1. **Registro:** Crie uma conta com email e senha
2. **Login:** Envie email e senha para `/api/auth/login`
3. **Token:** Receba um token JWT na resposta
4. **Usar Token:** Envie o token no header `Authorization: Bearer TOKEN` em todas as requisições protegidas
5. **Expiração:** O token dura 24 horas. Após isso, faça login novamente.

---

## 📝 Formato do Header de Autorização

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA3MDY3MjAwLCJleHAiOjE3MDcxNTM2MDB9...
```

**Importante:**
- Sempre inclua a palavra `Bearer` antes do token
- Espaço entre `Bearer` e o token é obrigatório
- O token é uma string longa sem espaços

---

## 🛠️ Usando com Different Ferramentas

### cURL
```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/gastos
```

### JavaScript/Fetch
```javascript
fetch('http://localhost:8080/api/gastos', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
```

### Axios
```javascript
axios.get('http://localhost:8080/api/gastos', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
```

### Postman
1. Aba **Authorization**
2. Type: **Bearer Token**
3. Cole o token no campo **Token**

### Insomnia
1. Aba **Auth**
2. Escolha **Bearer Token**
3. Cole o token no campo **TOKEN**

---

## ⏱️ Duração do Token

- **Tempo de vida:** 24 horas (86400000 ms)
- **Após expiração:** Faça login novamente
- **Renovação:** Não há refresh token. Faça novo login quando expirar.

---

## 🐛 Erros Comuns

### 401 Unauthorized - "Token inválido ou expirado"
**Causa:** Token expirou ou está malformado  
**Solução:** Faça login novamente

### 401 Unauthorized - "Email ou senha inválidos"
**Causa:** Credenciais incorretas no login  
**Solução:** Verifique email e senha

### 403 Forbidden
**Causa:** Token não enviado ou formato incorreto  
**Solução:** Certifique-se de enviar `Authorization: Bearer TOKEN`

### Token muito longo no terminal
**Solução:** Use variável de ambiente:
```bash
export TOKEN="seu_token"
curl -H "Authorization: Bearer $TOKEN" URL
```

---

## 📚 Documentação Completa

Para ver todos os comandos cURL disponíveis, consulte:
- `CURL_COMMANDS.md` - Documentação completa com todos os endpoints
- `QUICK_START.md` - Guia rápido de início

---

## 🔒 Segurança

✅ **Faça:**
- Use HTTPS em produção
- Armazene token com segurança
- Nunca compartilhe seu token
- Implemente logout no frontend (deletar token)

❌ **Não Faça:**
- Expor token em URLs
- Usar HTTP em produção
- Armazenar token em logs
- Compartilhar token entre usuários

---

**✅ Pronto!** Agora você pode usar JWT na API ControleGastos.

**🎉 Próximos passos:**
1. Execute o script de teste acima
2. Veja `CURL_COMMANDS.md` para mais exemplos
3. Integre com seu frontend

