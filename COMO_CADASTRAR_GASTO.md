# Como Cadastrar um Gasto

## 📋 Visão Geral

Para cadastrar um gasto na aplicação, você precisa:
1. Estar autenticado (ter um token JWT válido)
2. Enviar uma requisição POST com os dados do gasto
3. O gasto será associado automaticamente ao usuário logado

---

## 🔐 Passo 1: Fazer Login e Obter o Token JWT

Primeiro, faça login para obter o token de autenticação:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "senha": "admin123"
  }'
```

**Resposta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "admin@example.com",
  "nome": "Administrador"
}
```

**Copie o token** retornado, pois você precisará dele na próxima etapa.

---

## 💰 Passo 2: Cadastrar um Gasto

### Formato da Requisição

**Endpoint:** `POST /api/gastos`

**Headers necessários:**
- `Content-Type: application/json`
- `Authorization: Bearer SEU_TOKEN_JWT`

**Body (JSON):**
```json
{
  "tipo": "Mercado",
  "valor": 150.50
}
```

### Exemplo de cURL Completo

```bash
curl -X POST http://localhost:8080/api/gastos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "tipo": "Mercado",
    "valor": 150.50
  }'
```

**⚠️ Importante:** Substitua `SEU_TOKEN_AQUI` pelo token JWT que você recebeu no login!

---

## 📊 Categorias Disponíveis

Você pode usar as seguintes categorias no campo `tipo`:

| Categoria | Descrição |
|-----------|-----------|
| `Mercado` | Compras de supermercado |
| `Transporte` | Gastos com transporte |
| `Alimentação` | Restaurantes, lanches, etc. |
| `Saúde` | Gastos médicos e saúde |
| `Lazer` | Entretenimento e diversão |
| `Educação` | Cursos, livros, materiais |
| `Moradia` | Aluguel, contas de casa |
| `Outros` | Outras despesas |

---

## ✅ Exemplos de Cadastro

### Exemplo 1: Gasto com Mercado
```bash
curl -X POST http://localhost:8080/api/gastos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "tipo": "Mercado",
    "valor": 150.50
  }'
```

### Exemplo 2: Gasto com Transporte
```bash
curl -X POST http://localhost:8080/api/gastos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "tipo": "Transporte",
    "valor": 45.00
  }'
```

### Exemplo 3: Gasto com Alimentação
```bash
curl -X POST http://localhost:8080/api/gastos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "tipo": "Alimentação",
    "valor": 89.90
  }'
```

### Exemplo 4: Gasto com Saúde
```bash
curl -X POST http://localhost:8080/api/gastos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "tipo": "Saúde",
    "valor": 200.00
  }'
```

---

## 📤 Resposta Esperada

Quando o gasto é cadastrado com sucesso, você receberá uma resposta como esta:

**Status HTTP:** `201 Created`

**Body:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "valor": 150.50,
  "tipo": "Mercado"
}
```

---

## 🔍 Como Verificar seus Gastos Cadastrados

### Listar todos os gastos
```bash
curl -X GET http://localhost:8080/api/gastos \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### Buscar gastos por categoria
```bash
curl -X GET http://localhost:8080/api/gastos/tipo/Mercado \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### Buscar um gasto específico por ID
```bash
curl -X GET http://localhost:8080/api/gastos/123e4567-e89b-12d3-a456-426614174000 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 🔧 Usando Postman ou Insomnia

Se você preferir usar ferramentas gráficas:

### No Postman:
1. Crie uma nova requisição POST
2. URL: `http://localhost:8080/api/gastos`
3. Aba **Headers**:
   - Key: `Content-Type`, Value: `application/json`
   - Key: `Authorization`, Value: `Bearer SEU_TOKEN`
4. Aba **Body** → selecione **raw** e **JSON**:
   ```json
   {
     "tipo": "Mercado",
     "valor": 150.50
   }
   ```
5. Clique em **Send**

---

## ❌ Possíveis Erros

### 1. Erro 401 Unauthorized
**Causa:** Token JWT inválido, expirado ou não enviado

**Solução:** Faça login novamente e use o novo token

### 2. Erro 400 Bad Request
**Causa:** Dados inválidos no body (tipo ou valor incorreto)

**Solução:** Verifique se:
- O `tipo` está entre as categorias válidas
- O `valor` é um número válido (use ponto, não vírgula)

### 3. Erro 500 Internal Server Error
**Causa:** Problema no servidor (geralmente relacionado ao banco de dados)

**Solução:** Verifique os logs do servidor

---

## 🎯 Fluxo Completo (Passo a Passo)

```bash
# 1. Fazer login
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  -s | jq -r '.token')

# 2. Cadastrar um gasto usando o token
curl -X POST http://localhost:8080/api/gastos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "tipo": "Mercado",
    "valor": 150.50
  }'

# 3. Listar todos os gastos
curl -X GET http://localhost:8080/api/gastos \
  -H "Authorization: Bearer $TOKEN"
```

---

## 📝 Notas Importantes

1. **Autenticação obrigatória:** Todos os endpoints de gastos requerem autenticação
2. **Categoria automática:** Ao criar um gasto, a categoria é criada automaticamente se não existir
3. **Associação ao usuário:** O gasto é automaticamente associado ao usuário logado
4. **Validação:** O sistema valida se a categoria existe no enum `CategoriaEnum`

---

## 🚀 Próximos Passos

Após cadastrar gastos, você pode:
- ✅ Listar todos os gastos
- ✅ Filtrar gastos por categoria
- ✅ Atualizar gastos existentes
- ✅ Deletar gastos
- ✅ Visualizar relatórios por categoria

---

## 💡 Dica Pro

Para facilitar o desenvolvimento, você pode criar variáveis de ambiente no Postman:
- `base_url` = `http://localhost:8080`
- `token` = (o token JWT recebido no login)

Assim, você pode usar `{{base_url}}/api/gastos` e `Bearer {{token}}` nas requisições.

