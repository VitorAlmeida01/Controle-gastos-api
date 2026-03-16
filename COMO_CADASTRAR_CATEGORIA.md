# 📂 Como Cadastrar uma Categoria para o Usuário Logado

## 🎯 Como Funciona

O sistema **automaticamente associa a categoria ao usuário que está logado** através do token JWT. Você não precisa enviar o ID do usuário no body da requisição.

---

## 🚀 cURL para Cadastrar Categoria

### Endpoint
```
POST /api/categorias
```

### Passo 1: Fazer Login e Obter o Token

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  | jq -r '.token')

echo "Token obtido: ${TOKEN:0:50}..."
```

### Passo 2: Cadastrar a Categoria

```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "tipo": "Mercado"
  }'
```

---

## 📝 Body da Requisição

```json
{
  "tipo": "Mercado"
}
```

### Campo Obrigatório

| Campo | Tipo | Valores Permitidos |
|-------|------|-------------------|
| `tipo` | String | `Mercado`, `Transporte`, `Alimentação`, `Saúde`, `Lazer`, `Educação`, `Moradia`, `Outros` |

---

## ✅ Resposta de Sucesso (201 Created)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "tipo": "Mercado",
  "dtCriacao": "2026-03-14",
  "usuario": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "nome": "Admin",
    "email": "admin@example.com"
  }
}
```

---

## 🔍 Como o Sistema Identifica o Usuário

### 1. Token JWT no Header
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### 2. Extração do Email
O sistema extrai o email do token JWT automaticamente.

### 3. Busca no Banco
O sistema busca o usuário no banco de dados usando o email.

### 4. Associação
A categoria é criada e associada automaticamente ao usuário encontrado.

### Código no Controller:
```java
// Obter usuário autenticado
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
String email = authentication.getName();

// Buscar usuário no banco
Usuario usuario = usuarioService.buscarPorEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

// Cadastrar categoria para o usuário
Categoria categoriaSalva = categoriaService.cadastrarCategoria(usuario, categoriaDto);
```

---

## 📊 Exemplos Práticos

### Exemplo 1: Categoria Mercado

```bash
# Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  | jq -r '.token')

# Cadastrar categoria Mercado
curl -s -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"tipo":"Mercado"}' | jq '.'
```

**Resposta:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "tipo": "Mercado",
  "dtCriacao": "2026-03-14",
  "usuario": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "nome": "Admin",
    "email": "admin@example.com"
  }
}
```

---

### Exemplo 2: Categoria Transporte

```bash
curl -s -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"tipo":"Transporte"}' | jq '.'
```

---

### Exemplo 3: Categoria Saúde

```bash
curl -s -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"tipo":"Saúde"}' | jq '.'
```

---

## 🔄 Script Completo

```bash
#!/bin/bash

echo "==================================="
echo "   CADASTRO DE CATEGORIAS"
echo "==================================="
echo ""

# Fazer login
echo "1. Fazendo login..."
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  | jq -r '.token')

echo "   Token: ${TOKEN:0:50}..."
echo ""

# Cadastrar categorias
categorias=("Mercado" "Transporte" "Alimentação" "Saúde" "Lazer")

for categoria in "${categorias[@]}"; do
  echo "2. Cadastrando categoria: $categoria"
  
  curl -s -X POST http://localhost:8080/api/categorias \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d "{\"tipo\":\"$categoria\"}" | jq '.'
  
  echo ""
done

echo "==================================="
echo "   CATEGORIAS CADASTRADAS!"
echo "==================================="
```

---

## 👥 Diferentes Usuários, Diferentes Categorias

### Usuário 1 (Admin)

```bash
# Login como Admin
TOKEN_ADMIN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  | jq -r '.token')

# Cadastrar categoria para Admin
curl -s -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN_ADMIN" \
  -d '{"tipo":"Mercado"}' | jq '.'
```

**Resultado:** Categoria criada e associada ao usuário `admin@example.com`

---

### Usuário 2 (João)

```bash
# Login como João
TOKEN_JOAO=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@example.com","senha":"senha123"}' \
  | jq -r '.token')

# Cadastrar categoria para João
curl -s -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN_JOAO" \
  -d '{"tipo":"Mercado"}' | jq '.'
```

**Resultado:** Categoria criada e associada ao usuário `joao@example.com`

**📌 Importante:** Cada usuário tem suas próprias categorias! Mesmo que seja o mesmo tipo "Mercado", são categorias separadas no banco de dados.

---

## ❌ Erros Possíveis

### 401 Unauthorized - "Token inválido"
```json
{
  "timestamp": "2026-03-14T...",
  "status": 401,
  "error": "Unauthorized"
}
```
**Causa:** Token não enviado, inválido ou expirado  
**Solução:** Faça login novamente

---

### 400 Bad Request - "Usuário não encontrado"
```json
{
  "erro": "Usuário não encontrado"
}
```
**Causa:** O email do token não existe no banco de dados  
**Solução:** Verifique se o usuário foi cadastrado corretamente

---

### 400 Bad Request - "Tipo de categoria inválido"
```json
{
  "erro": "Tipo de categoria inválido: TipoErrado"
}
```
**Causa:** Tipo de categoria não existe no enum  
**Solução:** Use um dos tipos permitidos

---

### 500 Internal Server Error
```json
{
  "erro": "Erro ao cadastrar categoria: [mensagem]"
}
```
**Causa:** Erro no servidor (geralmente problema no banco)  
**Solução:** Verifique os logs do servidor

---

## 📱 Postman/Insomnia

### Configuração

1. **Método:** `POST`
2. **URL:** `http://localhost:8080/api/categorias`
3. **Headers:**
   - `Content-Type: application/json`
   - `Authorization: Bearer SEU_TOKEN`
4. **Body (raw JSON):**
```json
{
  "tipo": "Mercado"
}
```

### Passo a Passo

1. Faça login no endpoint `/api/auth/login`
2. Copie o token da resposta
3. Vá para a requisição `/api/categorias`
4. Adicione o token no header `Authorization: Bearer [token]`
5. No body, envie o JSON com o tipo da categoria
6. Clique em **Send**

---

## 🎨 Tipos de Categorias Disponíveis

| Tipo | Descrição |
|------|-----------|
| `Mercado` | Compras de supermercado |
| `Transporte` | Gastos com transporte (combustível, passagem, etc.) |
| `Alimentação` | Restaurantes, delivery, lanches |
| `Saúde` | Médicos, remédios, exames |
| `Lazer` | Entretenimento, cinema, viagens |
| `Educação` | Cursos, livros, materiais escolares |
| `Moradia` | Aluguel, contas de luz, água, internet |
| `Outros` | Outras despesas não categorizadas |

---

## 🔐 Segurança

✅ **O usuário só pode criar categorias para si mesmo**  
✅ **Não é possível criar categoria para outro usuário**  
✅ **O token JWT garante a identidade do usuário**  
✅ **Cada usuário tem suas próprias categorias isoladas**

---

## 💡 Dicas

### 1. Salvar o Token em uma Variável
```bash
export TOKEN="seu_token_aqui"

# Agora pode usar $TOKEN em todas as requisições
curl -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"tipo":"Mercado"}'
```

### 2. Verificar Categorias do Usuário
```bash
# Listar todas as categorias do usuário logado
curl -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN"
```

### 3. Cadastrar Múltiplas Categorias de uma Vez
```bash
for tipo in "Mercado" "Transporte" "Saúde"; do
  curl -s -X POST http://localhost:8080/api/categorias \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d "{\"tipo\":\"$tipo\"}"
  echo ""
done
```

---

## 📚 Documentos Relacionados

- [Login Body Guide](./LOGIN_BODY_GUIDE.md) - Como fazer login
- [Como Cadastrar Gasto](./COMO_CADASTRAR_GASTO.md) - Cadastrar gastos
- [cURL Commands](./CURL_COMMANDS.md) - Todos os comandos
- [JWT Login Guide](./JWT_LOGIN_GUIDE.md) - Guia completo JWT

