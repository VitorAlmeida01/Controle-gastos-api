# 🔍 Filtros de Gastos - Guia Completo

## ✅ Todos os Filtros Disponíveis

### 1. 🌐 Sem Filtro (Todos os Gastos)
```
GET /api/gastos
```

**Retorna:** Todos os gastos do usuário logado

```bash
curl -s -X GET http://localhost:8080/api/gastos \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

---

### 2. 🏷️ Por Categoria/Tipo
```
GET /api/gastos/tipo/{tipo}
```

**Retorna:** Todos os gastos de uma categoria específica

```bash
# Exemplos
curl -X GET http://localhost:8080/api/gastos/tipo/Mercado -H "Authorization: Bearer $TOKEN"
curl -X GET http://localhost:8080/api/gastos/tipo/Netflix -H "Authorization: Bearer $TOKEN"
curl -X GET http://localhost:8080/api/gastos/tipo/Transporte -H "Authorization: Bearer $TOKEN"
```

---

### 3. 📅 Por Período

#### 3.1 Dia Atual
```
GET /api/gastos/periodo/dia
```

#### 3.2 Semana Atual
```
GET /api/gastos/periodo/semana
```

#### 3.3 Mês Atual
```
GET /api/gastos/periodo/mes
```

#### 3.4 Últimos 6 Meses
```
GET /api/gastos/periodo/6meses
```

#### 3.5 Ano Atual
```
GET /api/gastos/periodo/ano
```

---

## 📊 Tabela Comparativa

| Filtro | Endpoint | Retorno | Total/Qtd |
|--------|----------|---------|-----------|
| Nenhum | `/api/gastos` | Array simples | ❌ |
| Por Tipo | `/api/gastos/tipo/{tipo}` | Array simples | ❌ |
| Por ID | `/api/gastos/{id}` | Objeto único | ❌ |
| Dia | `/api/gastos/periodo/dia` | Objeto com total | ✅ |
| Semana | `/api/gastos/periodo/semana` | Objeto com total | ✅ |
| Mês | `/api/gastos/periodo/mes` | Objeto com total | ✅ |
| 6 Meses | `/api/gastos/periodo/6meses` | Objeto com total | ✅ |
| Ano | `/api/gastos/periodo/ano` | Objeto com total | ✅ |

---

## 🎯 Casos de Uso Práticos

### 1. Dashboard Completo

```bash
TOKEN="seu_token"

echo "=== DASHBOARD DE GASTOS ==="
echo ""

# Gastos do dia
HOJE=$(curl -s -X GET http://localhost:8080/api/gastos/periodo/dia \
  -H "Authorization: Bearer $TOKEN")
echo "📅 Hoje: R$ $(echo $HOJE | jq '.total')"

# Gastos da semana
SEMANA=$(curl -s -X GET http://localhost:8080/api/gastos/periodo/semana \
  -H "Authorization: Bearer $TOKEN")
echo "📆 Semana: R$ $(echo $SEMANA | jq '.total')"

# Gastos do mês
MES=$(curl -s -X GET http://localhost:8080/api/gastos/periodo/mes \
  -H "Authorization: Bearer $TOKEN")
echo "📊 Mês: R$ $(echo $MES | jq '.total')"

# Gastos do ano
ANO=$(curl -s -X GET http://localhost:8080/api/gastos/periodo/ano \
  -H "Authorization: Bearer $TOKEN")
echo "📈 Ano: R$ $(echo $ANO | jq '.total')"
```

---

### 2. Análise por Categoria

```bash
TOKEN="seu_token"

# Listar categorias
CATEGORIAS=$(curl -s -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" | jq -r '.[].tipo')

echo "=== GASTOS POR CATEGORIA ==="
for categoria in $CATEGORIAS; do
  TOTAL=$(curl -s -X GET "http://localhost:8080/api/gastos/tipo/$categoria" \
    -H "Authorization: Bearer $TOKEN" \
    | jq '[.[].valor] | add // 0')
  echo "$categoria: R$ $TOTAL"
done
```

---

### 3. Top 3 Categorias com Mais Gastos

```bash
TOKEN="seu_token"

# Buscar todas as categorias
CATEGORIAS=$(curl -s -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" | jq -r '.[].tipo')

# Array temporário para armazenar
declare -a TOTAIS

for categoria in $CATEGORIAS; do
  TOTAL=$(curl -s -X GET "http://localhost:8080/api/gastos/tipo/$categoria" \
    -H "Authorization: Bearer $TOKEN" \
    | jq '[.[].valor] | add // 0')
  TOTAIS+=("$TOTAL:$categoria")
done

# Ordenar e mostrar top 3
echo "=== TOP 3 CATEGORIAS ==="
printf '%s\n' "${TOTAIS[@]}" | sort -rn -t: -k1 | head -3 | \
  awk -F: '{printf "%s: R$ %.2f\n", $2, $1}'
```

---

### 4. Gastos Recentes (Última Semana por Categoria)

```bash
TOKEN="seu_token"

# Buscar gastos da semana
curl -s -X GET http://localhost:8080/api/gastos/periodo/semana \
  -H "Authorization: Bearer $TOKEN" \
  | jq -r '.gastos[] | "\(.dtCriacao) - \(.tipo): R$ \(.valor)"' \
  | sort -r
```

---

### 5. Comparação: Mês Atual vs Categoria Específica

```bash
TOKEN="seu_token"

# Total do mês
TOTAL_MES=$(curl -s -X GET http://localhost:8080/api/gastos/periodo/mes \
  -H "Authorization: Bearer $TOKEN" | jq '.total')

# Total em Mercado
TOTAL_MERCADO=$(curl -s -X GET http://localhost:8080/api/gastos/tipo/Mercado \
  -H "Authorization: Bearer $TOKEN" | jq '[.[].valor] | add // 0')

# Calcular percentual
PERCENTUAL=$(echo "scale=2; ($TOTAL_MERCADO / $TOTAL_MES) * 100" | bc)

echo "Total do mês: R$ $TOTAL_MES"
echo "Gasto em Mercado: R$ $TOTAL_MERCADO"
echo "Percentual: $PERCENTUAL%"
```

---

## 🔄 Combinando Filtros (Exemplo Avançado)

```bash
#!/bin/bash

TOKEN="seu_token"

echo "=== RELATÓRIO DETALHADO ==="
echo ""

# 1. Gastos totais
TOTAL_GERAL=$(curl -s -X GET http://localhost:8080/api/gastos \
  -H "Authorization: Bearer $TOKEN" \
  | jq '[.[].valor] | add')
echo "💰 Total Geral: R$ $TOTAL_GERAL"
echo ""

# 2. Por período
echo "📊 Por Período:"
for periodo in "dia" "semana" "mes" "ano"; do
  RESULTADO=$(curl -s -X GET "http://localhost:8080/api/gastos/periodo/$periodo" \
    -H "Authorization: Bearer $TOKEN")
  TOTAL=$(echo $RESULTADO | jq '.total')
  QTD=$(echo $RESULTADO | jq '.quantidade')
  echo "  - $(echo $periodo | tr '[:lower:]' '[:upper:]'): R$ $TOTAL ($QTD gastos)"
done
echo ""

# 3. Por categoria
echo "🏷️  Por Categoria:"
CATEGORIAS=$(curl -s -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" | jq -r '.[].tipo')

for categoria in $CATEGORIAS; do
  GASTOS=$(curl -s -X GET "http://localhost:8080/api/gastos/tipo/$categoria" \
    -H "Authorization: Bearer $TOKEN")
  TOTAL=$(echo $GASTOS | jq '[.[].valor] | add // 0')
  QTD=$(echo $GASTOS | jq 'length')
  if [ "$QTD" -gt 0 ]; then
    echo "  - $categoria: R$ $TOTAL ($QTD gastos)"
  fi
done
```

---

## 📱 Exemplos para Postman/Insomnia

### Filtro por Categoria
```
GET http://localhost:8080/api/gastos/tipo/Mercado
Headers:
  Authorization: Bearer {{token}}
```

### Filtro por Período (Mês)
```
GET http://localhost:8080/api/gastos/periodo/mes
Headers:
  Authorization: Bearer {{token}}
```

---

## 💡 Dicas

### 1. Criar Variáveis de Ambiente
```bash
# No Postman/Insomnia, crie:
- {{baseUrl}} = http://localhost:8080
- {{token}} = seu_token_jwt

# Use assim:
GET {{baseUrl}}/api/gastos/tipo/Mercado
Headers: Authorization: Bearer {{token}}
```

### 2. Salvar Resposta em Arquivo
```bash
# Salvar gastos do mês em arquivo
curl -s -X GET http://localhost:8080/api/gastos/periodo/mes \
  -H "Authorization: Bearer $TOKEN" \
  | jq '.' > gastos_mes.json
```

### 3. Formato CSV para Excel
```bash
# Exportar gastos para CSV
curl -s -X GET http://localhost:8080/api/gastos/periodo/mes \
  -H "Authorization: Bearer $TOKEN" \
  | jq -r '.gastos[] | [.dtCriacao, .tipo, .valor] | @csv' \
  > gastos.csv
```

---

## 🎯 Resumo Rápido

**Filtros Disponíveis:**
- ✅ Sem filtro (todos)
- ✅ Por categoria/tipo
- ✅ Por dia
- ✅ Por semana
- ✅ Por mês
- ✅ Por 6 meses
- ✅ Por ano
- ✅ Por ID específico

**Endpoints que retornam total + quantidade:**
- ✅ Períodos (dia, semana, mês, 6 meses, ano)

**Endpoints que retornam array simples:**
- ✅ Todos os gastos
- ✅ Por tipo/categoria
- ✅ Por ID

**Você tem filtros completos para qualquer análise de gastos!** 🔍📊

