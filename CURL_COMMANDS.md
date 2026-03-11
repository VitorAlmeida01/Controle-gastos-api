# 🚀 Comandos cURL - API ControleGastos

Base URL: `http://localhost:8080`

---

## ⚡ Quick Start - Login com JWT

```bash
# 1. REGISTRAR (sem autenticação)
curl -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"Seu Nome","email":"email@example.com","senha":"suasenha"}'

# 2. LOGIN (receber token JWT)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"email@example.com","senha":"suasenha"}'

# Resposta do login:
# {
#   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "tipo": "Bearer",
#   "email": "email@example.com",
#   "nome": "Seu Nome"
# }

# 3. USAR TOKEN em requisições protegidas
export TOKEN="seu_token_aqui"
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/gastos
```

**📌 Nota:** Esta API agora suporta **JWT (JSON Web Token)**. Após o login, use o token retornado em todas as requisições protegidas.

---

## 📋 Índice
1. [🔐 Autenticação / Login com JWT](#-autenticação--login-com-jwt)
2. [👤 Usuários](#-usuários)
3. [📂 Categorias](#-categorias)
4. [💰 Gastos](#-gastos)
5. [🔧 Utilitários](#-utilitários)

---

## 🔐 Autenticação / Login com JWT

### 1. Fazer Login (Obter Token JWT)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "senha123"
  }'
```

**Resposta de sucesso (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA3MDY3MjAwLCJleHAiOjE3MDcxNTM2MDB9.xyz...",
  "tipo": "Bearer",
  "email": "joao@example.com",
  "nome": "João Silva"
}
```

**Resposta de erro (401 Unauthorized):**
```json
"Email ou senha inválidos"
```

### 2. Validar Token

```bash
curl -X GET http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta se token válido:**
```json
"Token válido"
```

**Resposta se token inválido:**
```json
"Token inválido ou expirado"
```

### 3. Usar Token em Requisições

**Método 1: Com variável de ambiente (Recomendado)**
```bash
# Fazer login e salvar token
export TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@example.com","senha":"senha123"}' \
  | jq -r '.token')

# Usar token em requisições
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/gastos
```

**Método 2: Token direto no comando**
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  http://localhost:8080/api/gastos
```

### 4. Fluxo Completo: Registro → Login → Operação

```bash
# Passo 1: Registrar novo usuário
curl -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "email": "maria@example.com",
    "senha": "senha456"
  }'

# Passo 2: Fazer login e obter token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria@example.com",
    "senha": "senha456"
  }'

# Copie o token da resposta e use-o:

# Passo 3: Listar gastos (autenticado com JWT)
curl -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  http://localhost:8080/api/gastos

# Passo 4: Criar gasto (autenticado com JWT)
curl -X POST http://localhost:8080/api/gastos \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "valor": 150.50,
    "categoria": {"id": "uuid-da-categoria"}
  }'
```

### 5. Script Completo com Login Automático

```bash
#!/bin/bash

# Configurações
API_URL="http://localhost:8080"
EMAIL="joao@example.com"
SENHA="senha123"

# 1. Fazer login e extrair token
echo "🔐 Fazendo login..."
RESPONSE=$(curl -s -X POST ${API_URL}/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"${EMAIL}\",\"senha\":\"${SENHA}\"}")

TOKEN=$(echo $RESPONSE | jq -r '.token')
NOME=$(echo $RESPONSE | jq -r '.nome')

if [ "$TOKEN" != "null" ] && [ "$TOKEN" != "" ]; then
  echo "✅ Login bem-sucedido! Bem-vindo, $NOME"
  echo "🎫 Token: ${TOKEN:0:50}..."
  
  # 2. Usar token para listar gastos
  echo ""
  echo "📊 Listando gastos..."
  curl -s -H "Authorization: Bearer $TOKEN" \
    ${API_URL}/api/gastos | jq '.'
    
  # 3. Ver total de gastos
  echo ""
  echo "💰 Total de gastos:"
  curl -s -H "Authorization: Bearer $TOKEN" \
    ${API_URL}/api/gastos/total
else
  echo "❌ Falha no login. Verifique email e senha."
fi
```

### 📝 Informações do Token JWT

**Duração do Token:** 24 horas (86400000 ms)

**Após expirar:** Faça login novamente para obter um novo token

**Estrutura do Token:**
- **Header:** Algoritmo e tipo (HS256, JWT)
- **Payload:** Email do usuário, data de emissão, data de expiração
- **Signature:** Assinatura criptográfica

**Decodificar token (apenas para visualizar - não valida):**
```bash
# Exemplo com jwt.io ou comando
echo "SEU_TOKEN" | cut -d'.' -f2 | base64 -d | jq '.'
```

### 🔒 Segurança JWT

**✅ Boas Práticas:**
- Armazene o token com segurança (não em cookies sem flags)
- Use HTTPS em produção
- Não compartilhe seu token
- Faça logout deletando o token do cliente
- Token expira automaticamente em 24h

**❌ Não Faça:**
- Armazenar token em localStorage sem criptografia adicional
- Expor token em URLs ou logs
- Usar HTTP em produção (sempre HTTPS)

### ❓ FAQ - JWT

**P: O que é JWT?**
R: JSON Web Token - um padrão de token assinado para autenticação stateless.

**P: Preciso fazer login toda vez?**
R: Não! O token dura 24 horas. Salve-o e reutilize até expirar.

**P: Como fazer logout?**
R: Simplesmente descarte o token no cliente. O servidor não mantém estado.

**P: Posso usar o mesmo token em múltiplas requisições?**
R: Sim! Use o mesmo token até expirar (24h).

**P: O que acontece quando o token expira?**
R: Você receberá erro 401. Faça login novamente para obter um novo token.

---

## 👤 Usuários

### 1. Registrar Novo Usuário (Sem autenticação)
```bash
curl -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123"
  }'
```

**Resposta esperada:**
```json
{
  "id": "uuid-gerado",
  "nome": "João Silva",
  "email": "joao@example.com"
}
```

### 2. Listar Todos os Usuários (Requer autenticação)
```bash
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/usuarios
```

**Resposta esperada:**
```json
[
  {
    "id": "uuid-gerado",
    "nome": "João Silva",
    "email": "joao@example.com"
  }
]
```

---

## 📂 Categorias

### 1. Listar Enums de Categorias
```bash
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/categorias
```

**Valores disponíveis:**
- MERCADO
- TRANSPORTE
- ALIMENTACAO
- SAUDE
- LAZER
- EDUCACAO
- MORADIA
- OUTROS

---

## 💰 Gastos

### 1. Criar Novo Gasto
```bash
curl -X POST http://localhost:8080/api/gastos \
  -u joao@example.com:senha123 \
  -H "Content-Type: application/json" \
  -d '{
    "valor": 150.50,
    "categoria": {
      "id": "uuid-da-categoria"
    }
  }'
```

**Exemplo com categoria completa:**
```bash
curl -X POST http://localhost:8080/api/gastos \
  -u joao@example.com:senha123 \
  -H "Content-Type: application/json" \
  -d '{
    "valor": 150.50,
    "categoria": {
      "tipo": "MERCADO",
      "usuario": {
        "id": "uuid-do-usuario"
      }
    }
  }'
```

### 2. Listar Todos os Gastos
```bash
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos
```

### 3. Buscar Gasto por ID
```bash
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/UUID-DO-GASTO
```

**Exemplo:**
```bash
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/123e4567-e89b-12d3-a456-426614174000
```

### 4. Buscar Gastos por Tipo de Categoria
```bash
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/tipo/MERCADO
```

**Outros exemplos:**
```bash
# Gastos de transporte
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/tipo/TRANSPORTE

# Gastos com alimentação
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/tipo/ALIMENTACAO

# Gastos com saúde
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/tipo/SAUDE
```

### 5. Atualizar Gasto
```bash
curl -X PUT http://localhost:8080/api/gastos/UUID-DO-GASTO \
  -u joao@example.com:senha123 \
  -H "Content-Type: application/json" \
  -d '{
    "valor": 200.00,
    "categoria": {
      "id": "uuid-da-categoria"
    }
  }'
```

### 6. Deletar Gasto
```bash
curl -X DELETE http://localhost:8080/api/gastos/UUID-DO-GASTO \
  -u joao@example.com:senha123
```

### 7. Calcular Total de Todos os Gastos
```bash
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/total
```

**Resposta esperada:**
```json
1523.45
```

### 8. Calcular Total por Tipo de Categoria
```bash
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/total/tipo/MERCADO
```

**Outros exemplos:**
```bash
# Total gasto com transporte
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/total/tipo/TRANSPORTE

# Total gasto com alimentação
curl -u joao@example.com:senha123 \
  http://localhost:8080/api/gastos/total/tipo/ALIMENTACAO
```

---

## 🔧 Utilitários

### H2 Console (Acesso ao banco de dados)
```bash
# Abrir no navegador
open http://localhost:8080/h2-console

# Configurações de conexão:
# JDBC URL: jdbc:h2:mem:testdb
# Username: sa
# Password: (vazio)
```

### Teste de CORS (Pré-flight)
```bash
curl -i -X OPTIONS http://localhost:8080/api/gastos \
  -H "Origin: http://localhost:4200" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type"
```

---

## 📝 Fluxo Completo de Teste

### Passo 1: Registrar Usuário
```bash
curl -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "email": "maria@example.com",
    "senha": "senha456"
  }'
```

### Passo 2: Criar Categoria (Primeiro precisa criar manualmente via SQL ou endpoint de categoria)
> **Nota:** Como o CategoriaController está vazio, você pode precisar criar categorias diretamente no banco via H2 Console

**Inserir categoria via H2 Console:**
```sql
INSERT INTO CATEGORIA (ID, TIPO, DT_CRIACAO, USUARIO_ID) 
VALUES (RANDOM_UUID(), 'MERCADO', CURRENT_DATE, 'uuid-do-usuario');
```

### Passo 3: Criar Gasto
```bash
# Substitua os UUIDs pelos valores reais do seu banco
curl -X POST http://localhost:8080/api/gastos \
  -u maria@example.com:senha456 \
  -H "Content-Type: application/json" \
  -d '{
    "valor": 85.90,
    "categoria": {
      "id": "uuid-da-categoria-criada"
    }
  }'
```

### Passo 4: Listar Gastos
```bash
curl -u maria@example.com:senha456 \
  http://localhost:8080/api/gastos
```

### Passo 5: Ver Total
```bash
curl -u maria@example.com:senha456 \
  http://localhost:8080/api/gastos/total
```

---

## 🔐 Notas de Autenticação

**HTTP Basic Auth:**
- Formato: `-u email:senha`
- Header equivalente: `-H "Authorization: Basic base64(email:senha)"`

**Exemplo de header manual:**
```bash
# Codificar credenciais em base64
echo -n "joao@example.com:senha123" | base64

# Usar no header
curl -H "Authorization: Basic am9hb0BleGFtcGxlLmNvbTpzZW5oYTEyMw==" \
  http://localhost:8080/api/gastos
```

---

## ⚙️ Variáveis de Ambiente (Opcional)

Para facilitar os testes, você pode definir variáveis:

```bash
# Definir variáveis
export API_URL="http://localhost:8080"
export USER_EMAIL="joao@example.com"
export USER_PASS="senha123"

# Usar nos comandos
curl -u $USER_EMAIL:$USER_PASS $API_URL/api/gastos
```

---

## 🐛 Troubleshooting

### Erro 401 (Unauthorized)
- Verifique se o usuário foi registrado
- Confirme email e senha corretos
- Teste sem autenticação no endpoint `/register`

### Erro 403 (Forbidden)
- CSRF pode estar bloqueando (mas está desabilitado para /api/**)
- Verifique se está usando o método HTTP correto

### Erro 404 (Not Found)
- Confirme a URL e o ID do recurso
- Verifique se o servidor está rodando na porta 8080

### Erro 500 (Internal Server Error)
- Verifique os logs do servidor
- Confirme se o banco de dados H2 está funcionando
- Valide o JSON enviado no body

---

## 📊 Exemplos de Respostas

### Sucesso (200/201)
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "valor": 150.50,
  "categoria": {
    "id": "223e4567-e89b-12d3-a456-426614174001",
    "tipo": "MERCADO",
    "dtCriacao": "2026-02-04"
  }
}
```

### Erro de Validação (400)
```json
{
  "timestamp": "2026-02-04T15:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed"
}
```

### Não Autorizado (401)
```json
{
  "timestamp": "2026-02-04T15:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required"
}
```

---

## 🎯 Dicas Úteis

1. **Pretty Print JSON com jq:**
```bash
curl -u email:senha http://localhost:8080/api/gastos | jq '.'
```

2. **Salvar resposta em arquivo:**
```bash
curl -u email:senha http://localhost:8080/api/gastos > gastos.json
```

3. **Ver headers da resposta:**
```bash
curl -i -u email:senha http://localhost:8080/api/gastos
```

4. **Verbose mode (debug):**
```bash
curl -v -u email:senha http://localhost:8080/api/gastos
```

5. **Timeout:**
```bash
curl --max-time 30 -u email:senha http://localhost:8080/api/gastos
```

---

**✅ Arquivo gerado em:** `2026-02-04`  
**📦 Versão da API:** `0.0.1-SNAPSHOT`  
**🔧 Spring Boot:** `4.0.2`

