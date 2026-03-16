# 📅 Gastos por Período - Data de Criação

## ✅ O que foi implementado

1. ✅ **Campo `dtCriacao`** adicionado na entidade `Gasto`
2. ✅ **Data automática** ao criar um gasto
3. ✅ **Endpoints para filtrar** gastos por período
4. ✅ **Total e quantidade** retornados em cada consulta

---

## 📊 Estrutura do Gasto com Data

### Resposta ao cadastrar/listar gastos:

```json
{
  "id": "789e4567-e89b-12d3-a456-426614174789",
  "valor": 150.50,
  "tipo": "Mercado",
  "dtCriacao": "2026-03-15T14:30:00"
}
```

---

## 🎯 Novos Endpoints

### 1️⃣ Gastos do Dia

**Endpoint:** `GET /api/gastos/periodo/dia`

```bash
curl -s -X GET http://localhost:8080/api/gastos/periodo/dia \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
{
  "gastos": [
    {
      "id": "uuid-1",
      "valor": 150.50,
      "tipo": "Mercado",
      "dtCriacao": "2026-03-15T10:30:00"
    },
    {
      "id": "uuid-2",
      "valor": 45.90,
      "tipo": "Netflix",
      "dtCriacao": "2026-03-15T14:20:00"
    }
  ],
  "total": 196.40,
  "quantidade": 2
}
```

---

### 2️⃣ Gastos da Semana

**Endpoint:** `GET /api/gastos/periodo/semana`

Retorna gastos de **segunda a domingo** da semana atual.

```bash
curl -s -X GET http://localhost:8080/api/gastos/periodo/semana \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
{
  "gastos": [
    {
      "id": "uuid-1",
      "valor": 150.50,
      "tipo": "Mercado",
      "dtCriacao": "2026-03-10T10:30:00"
    },
    {
      "id": "uuid-2",
      "valor": 45.90,
      "tipo": "Transporte",
      "dtCriacao": "2026-03-12T08:15:00"
    },
    {
      "id": "uuid-3",
      "valor": 89.00,
      "tipo": "Alimentação",
      "dtCriacao": "2026-03-15T12:45:00"
    }
  ],
  "total": 285.40,
  "quantidade": 3
}
```

---

### 3️⃣ Gastos do Mês

**Endpoint:** `GET /api/gastos/periodo/mes`

Retorna gastos do **mês atual** (do dia 1 ao último dia do mês).

```bash
curl -s -X GET http://localhost:8080/api/gastos/periodo/mes \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
{
  "gastos": [
    {
      "id": "uuid-1",
      "valor": 450.00,
      "tipo": "Mercado",
      "dtCriacao": "2026-03-01T09:00:00"
    },
    {
      "id": "uuid-2",
      "valor": 89.90,
      "tipo": "Netflix",
      "dtCriacao": "2026-03-05T10:30:00"
    },
    {
      "id": "uuid-3",
      "valor": 250.00,
      "tipo": "Transporte",
      "dtCriacao": "2026-03-15T14:20:00"
    }
  ],
  "total": 789.90,
  "quantidade": 3
}
```

---

### 4️⃣ Gastos dos Últimos 6 Meses

**Endpoint:** `GET /api/gastos/periodo/6meses`

Retorna gastos dos **últimos 6 meses** até hoje.

```bash
curl -s -X GET http://localhost:8080/api/gastos/periodo/6meses \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
{
  "gastos": [
    {
      "id": "uuid-1",
      "valor": 1500.00,
      "tipo": "Mercado",
      "dtCriacao": "2025-10-15T10:30:00"
    },
    {
      "id": "uuid-2",
      "valor": 890.00,
      "tipo": "Academia",
      "dtCriacao": "2025-12-20T08:00:00"
    }
  ],
  "total": 5432.10,
  "quantidade": 15
}
```

---

### 5️⃣ Gastos do Ano

**Endpoint:** `GET /api/gastos/periodo/ano`

Retorna gastos do **ano atual** (01/janeiro até 31/dezembro).

```bash
curl -s -X GET http://localhost:8080/api/gastos/periodo/ano \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
{
  "gastos": [
    {
      "id": "uuid-1",
      "valor": 3500.00,
      "tipo": "Mercado",
      "dtCriacao": "2026-01-10T10:30:00"
    },
    {
      "id": "uuid-2",
      "valor": 1200.00,
      "tipo": "Transporte",
      "dtCriacao": "2026-02-15T14:20:00"
    }
  ],
  "total": 12450.00,
  "quantidade": 42
}
```

---

---

### 6️⃣ Filtrar por Categoria (Tipo)

**Endpoint:** `GET /api/gastos/tipo/{tipo}`

Retorna todos os gastos de uma categoria específica.

```bash
# Buscar gastos da categoria "Mercado"
curl -s -X GET http://localhost:8080/api/gastos/tipo/Mercado \
  -H "Authorization: Bearer $TOKEN" | jq '.'

# Buscar gastos da categoria "Netflix"
curl -s -X GET http://localhost:8080/api/gastos/tipo/Netflix \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
[
  {
    "id": "uuid-1",
    "valor": 150.50,
    "tipo": "Mercado",
    "dtCriacao": "2026-03-15T10:30:00"
  },
  {
    "id": "uuid-2",
    "valor": 89.90,
    "tipo": "Mercado",
    "dtCriacao": "2026-03-10T14:20:00"
  }
]
```

---

## 📋 Resumo dos Endpoints

| Endpoint | Filtro | Descrição |
|----------|--------|-----------|
| `GET /api/gastos` | Nenhum | Todos os gastos |
| `GET /api/gastos/tipo/{tipo}` | Categoria | Gastos de uma categoria específica |
| `GET /api/gastos/periodo/dia` | Hoje | Gastos do dia atual |
| `GET /api/gastos/periodo/semana` | Seg-Dom | Gastos da semana atual |
| `GET /api/gastos/periodo/mes` | Mês atual | Gastos do mês corrente |
| `GET /api/gastos/periodo/6meses` | Últimos 6 meses | Gastos dos últimos 180 dias |
| `GET /api/gastos/periodo/ano` | Ano atual | Gastos do ano corrente |

---

## 🔄 Fluxo Completo de Exemplo

```bash
#!/bin/bash

# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  | jq -r '.token')

echo "Token obtido: ${TOKEN:0:50}..."
echo ""

# 2. Criar categoria
CATEGORIA_ID=$(curl -s -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Mercado"}' | jq -r '.id')

echo "Categoria criada: $CATEGORIA_ID"
echo ""

# 3. Cadastrar alguns gastos
echo "Cadastrando gastos..."
curl -s -X POST http://localhost:8080/api/gastos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"categoriaId\":\"$CATEGORIA_ID\",\"valor\":150.50}" | jq '.'

curl -s -X POST http://localhost:8080/api/gastos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"categoriaId\":\"$CATEGORIA_ID\",\"valor\":89.90}" | jq '.'

echo ""

# 4. Consultar gastos do dia
echo "=== GASTOS DO DIA ==="
curl -s -X GET http://localhost:8080/api/gastos/periodo/dia \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

# 5. Consultar gastos da semana
echo "=== GASTOS DA SEMANA ==="
curl -s -X GET http://localhost:8080/api/gastos/periodo/semana \
  -H "Authorization: Bearer $TOKEN" | jq '.'
echo ""

# 6. Consultar gastos do mês
echo "=== GASTOS DO MÊS ==="
curl -s -X GET http://localhost:8080/api/gastos/periodo/mes \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

---

## 📊 Formato da Resposta

Todos os endpoints de período retornam o mesmo formato:

```json
{
  "gastos": [
    {
      "id": "uuid",
      "valor": 150.50,
      "tipo": "Categoria",
      "dtCriacao": "2026-03-15T14:30:00"
    }
  ],
  "total": 150.50,
  "quantidade": 1
}
```

### Campos da Resposta

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `gastos` | Array | Lista de gastos do período |
| `total` | Double | Soma total dos gastos |
| `quantidade` | Integer | Número de gastos encontrados |

---

## 🎯 Casos de Uso

### 1. Dashboard do Dia
```bash
TOKEN="seu_token"

# Pegar gastos e total do dia
RESULTADO=$(curl -s -X GET http://localhost:8080/api/gastos/periodo/dia \
  -H "Authorization: Bearer $TOKEN")

TOTAL_DIA=$(echo "$RESULTADO" | jq '.total')
QUANTIDADE_DIA=$(echo "$RESULTADO" | jq '.quantidade')

echo "Hoje você gastou: R$ $TOTAL_DIA em $QUANTIDADE_DIA compras"
```

---

### 2. Comparar Gastos do Mês vs Semana
```bash
TOKEN="seu_token"

# Total do mês
TOTAL_MES=$(curl -s -X GET http://localhost:8080/api/gastos/periodo/mes \
  -H "Authorization: Bearer $TOKEN" | jq '.total')

# Total da semana
TOTAL_SEMANA=$(curl -s -X GET http://localhost:8080/api/gastos/periodo/semana \
  -H "Authorization: Bearer $TOKEN" | jq '.total')

echo "Total do mês: R$ $TOTAL_MES"
echo "Total da semana: R$ $TOTAL_SEMANA"
```

---

### 3. Relatório Anual
```bash
TOKEN="seu_token"

curl -s -X GET http://localhost:8080/api/gastos/periodo/ano \
  -H "Authorization: Bearer $TOKEN" | jq '{
    "Total Anual": .total,
    "Quantidade de Gastos": .quantidade,
    "Média por Gasto": (.total / .quantidade)
  }'
```

---

### 4. Gastos por Categoria Específica
```bash
TOKEN="seu_token"

# Ver todos os gastos com "Mercado"
curl -s -X GET http://localhost:8080/api/gastos/tipo/Mercado \
  -H "Authorization: Bearer $TOKEN" | jq '.'

# Calcular total gasto em Netflix
TOTAL_NETFLIX=$(curl -s -X GET http://localhost:8080/api/gastos/tipo/Netflix \
  -H "Authorization: Bearer $TOKEN" \
  | jq '[.[].valor] | add')

echo "Total gasto em Netflix: R$ $TOTAL_NETFLIX"
```

---

### 5. Comparar Categorias
```bash
TOKEN="seu_token"

# Total em Mercado
TOTAL_MERCADO=$(curl -s -X GET http://localhost:8080/api/gastos/tipo/Mercado \
  -H "Authorization: Bearer $TOKEN" | jq '[.[].valor] | add')

# Total em Transporte
TOTAL_TRANSPORTE=$(curl -s -X GET http://localhost:8080/api/gastos/tipo/Transporte \
  -H "Authorization: Bearer $TOKEN" | jq '[.[].valor] | add')

echo "Mercado: R$ $TOTAL_MERCADO"
echo "Transporte: R$ $TOTAL_TRANSPORTE"
```

---

## 💡 Dicas

### 1. Salvar Token em Variável
```bash
export TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"seu@email.com","senha":"sua_senha"}' \
  | jq -r '.token')

# Agora use $TOKEN em todas as requisições
```

### 2. Formatar Data para Leitura
```bash
curl -s -X GET http://localhost:8080/api/gastos/periodo/dia \
  -H "Authorization: Bearer $TOKEN" \
  | jq '.gastos[] | {
      valor: .valor,
      tipo: .tipo,
      data: .dtCriacao
    }'
```

### 3. Filtrar Apenas Total
```bash
# Apenas o total do mês
curl -s -X GET http://localhost:8080/api/gastos/periodo/mes \
  -H "Authorization: Bearer $TOKEN" | jq '.total'
```

---

## 🔐 Segurança

✅ **Todos os endpoints requerem autenticação**  
✅ **Cada usuário vê apenas seus próprios gastos**  
✅ **Filtragem automática por usuário**  
✅ **Não é possível ver gastos de outros usuários**

---

## 📅 Como a Data é Salva

A data é **automaticamente** salva quando você cria um gasto:

```java
private LocalDateTime dtCriacao = LocalDateTime.now();
```

- ✅ Formato: `2026-03-15T14:30:00`
- ✅ Inclui data e hora
- ✅ Fuso horário do servidor
- ✅ Não pode ser alterada manualmente

---

## 🎯 Resumo

**Campo Adicionado:**
- ✅ `dtCriacao` (LocalDateTime) em todos os gastos

**Novos Endpoints:**
- ✅ `/api/gastos/periodo/dia` - Gastos de hoje
- ✅ `/api/gastos/periodo/semana` - Gastos da semana
- ✅ `/api/gastos/periodo/mes` - Gastos do mês
- ✅ `/api/gastos/periodo/6meses` - Últimos 6 meses
- ✅ `/api/gastos/periodo/ano` - Gastos do ano

**Informações Retornadas:**
- ✅ Lista de gastos
- ✅ Total do período
- ✅ Quantidade de gastos

**Agora você pode acompanhar seus gastos por período de forma organizada!** 📊🎉

