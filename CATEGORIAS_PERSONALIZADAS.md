# 🎨 Categorias Personalizadas por Usuário

## ✅ Mudança Implementada

Agora os usuários podem criar suas **próprias categorias personalizadas** sem limitações de um enum fixo no backend!

---

## 🔄 O que Mudou?

### ❌ ANTES (Com Enum - Limitado)

```java
public enum CategoriaEnum {
    MERCADO, TRANSPORTE, ALIMENTACAO, SAUDE, 
    LAZER, EDUCACAO, MORADIA, OUTROS
}
```

**Problemas:**
- ❌ Usuário limitado a 8 categorias fixas
- ❌ Não pode criar categorias personalizadas
- ❌ Backend precisa ser alterado para adicionar novas categorias

---

### ✅ AGORA (String - Flexível)

```java
@Entity
public class Categoria {
    @Column(nullable = false)
    private String tipo; // Agora aceita qualquer string!
}
```

**Vantagens:**
- ✅ Usuário cria quantas categorias quiser
- ✅ Nomes personalizados (ex: "Meus Pets", "Netflix", "Gym")
- ✅ Backend não precisa ser alterado
- ✅ Cada usuário tem suas próprias categorias

---

## 🚀 Como Usar

### 1️⃣ Criar Categoria Personalizada

```bash
TOKEN="seu_token"

# Criar categoria personalizada
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "tipo": "Meus Pets"
  }'
```

**Resposta:**
```json
{
  "id": "40342540-3a95-4902-8dbc-a7d1b295f236",
  "tipo": "Meus Pets",
  "dtCriacao": "2026-03-15"
}
```

---

### 2️⃣ Exemplos de Categorias Personalizadas

```bash
# Categoria para Netflix
curl -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Netflix"}'

# Categoria para Academia
curl -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Academia"}'

# Categoria para Cuidados com Pets
curl -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Veterinário"}'

# Categoria para Presentes
curl -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Presentes"}'
```

---

### 3️⃣ Listar Suas Categorias

```bash
curl -s -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
[
  {
    "id": "uuid-1",
    "tipo": "Mercado",
    "dtCriacao": "2026-03-15"
  },
  {
    "id": "uuid-2",
    "tipo": "Netflix",
    "dtCriacao": "2026-03-15"
  },
  {
    "id": "uuid-3",
    "tipo": "Meus Pets",
    "dtCriacao": "2026-03-15"
  },
  {
    "id": "uuid-4",
    "tipo": "Academia",
    "dtCriacao": "2026-03-15"
  }
]
```

---

### 4️⃣ Usar Categoria no Gasto

```bash
# Pegar ID da categoria "Netflix"
CATEGORIA_NETFLIX=$(curl -s -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  | jq -r '.[] | select(.tipo=="Netflix") | .id')

# Cadastrar gasto com essa categoria
curl -X POST http://localhost:8080/api/gastos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{
    \"categoriaId\": \"$CATEGORIA_NETFLIX\",
    \"valor\": 45.90
  }"
```

**Resposta:**
```json
{
  "id": "789e4567-e89b-12d3-a456-426614174789",
  "valor": 45.90,
  "tipo": "Netflix"
}
```

---

## 🔐 Validações Implementadas

### 1. Categoria Duplicada

```bash
# Tentar criar categoria que já existe
curl -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Netflix"}'
```

**Resposta:**
```json
{
  "erro": "Categoria com esse nome já existe para este usuário"
}
```

### 2. Isolamento entre Usuários

- ✅ Cada usuário tem suas próprias categorias
- ✅ Usuário A pode ter categoria "Academia"
- ✅ Usuário B pode ter categoria "Academia" também (com ID diferente)
- ✅ Não há conflito entre categorias de usuários diferentes

---

## 📊 Comparação Completa

### Antes (Enum)
```json
// POST /api/categorias
{
  "tipo": "MERCADO"  // ❌ Apenas valores fixos do enum
}
```

### Agora (String)
```json
// POST /api/categorias
{
  "tipo": "Qualquer Nome Que Você Quiser 🎉"
}
```

---

## 🎯 Casos de Uso

### Usuário 1: Estudante

```bash
TOKEN_ESTUDANTE="..."

# Criar categorias personalizadas
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_ESTUDANTE" -d '{"tipo":"Material Escolar"}'
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_ESTUDANTE" -d '{"tipo":"Xerox"}'
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_ESTUDANTE" -d '{"tipo":"Lanche na Cantina"}'
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_ESTUDANTE" -d '{"tipo":"Transporte para Aula"}'
```

---

### Usuário 2: Empreendedor

```bash
TOKEN_EMPREENDEDOR="..."

# Criar categorias de negócio
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_EMPREENDEDOR" -d '{"tipo":"Marketing Digital"}'
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_EMPREENDEDOR" -d '{"tipo":"Hospedagem Site"}'
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_EMPREENDEDOR" -d '{"tipo":"Fornecedores"}'
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_EMPREENDEDOR" -d '{"tipo":"Impostos"}'
```

---

### Usuário 3: Pet Owner

```bash
TOKEN_PET="..."

# Criar categorias para pets
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_PET" -d '{"tipo":"Ração"}'
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_PET" -d '{"tipo":"Veterinário"}'
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_PET" -d '{"tipo":"Petshop"}'
curl -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN_PET" -d '{"tipo":"Banho e Tosa"}'
```

---

## 🎨 Sugestões de Categorias

### Pessoais
- Mercado
- Transporte
- Alimentação
- Saúde
- Lazer
- Educação
- Moradia
- Outros

### Assinaturas/Streaming
- Netflix
- Spotify
- Amazon Prime
- Disney+
- YouTube Premium
- Apple Music

### Fitness/Saúde
- Academia
- Personal Trainer
- Suplementos
- Plano de Saúde
- Medicamentos

### Veículos
- Combustível
- Manutenção
- IPVA
- Seguro do Carro
- Estacionamento

### Pets
- Ração
- Veterinário
- Petshop
- Banho e Tosa
- Brinquedos

### Profissionais
- Materiais de Trabalho
- Cursos Online
- Software/Licenças
- Coworking
- Networking

---

## 📝 Formato do Body

### Criar Categoria
```json
{
  "tipo": "Nome da Categoria"
}
```

### Campos

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `tipo` | String | ✅ Sim | Nome da categoria (pode ser qualquer texto) |

---

## ⚠️ Regras e Limitações

### ✅ Permitido:
- Criar quantas categorias quiser
- Usar qualquer nome (letras, números, espaços, emojis)
- Ter nomes longos ou curtos
- Reutilizar nomes entre usuários diferentes

### ❌ Não Permitido:
- Criar categoria duplicada para o mesmo usuário
- Nome vazio ou null
- Usar categoria de outro usuário

---

## 🔄 Migração das Categorias Antigas

Se você tinha categorias baseadas no enum antigo, elas continuam funcionando! As categorias antigas com valores do enum (MERCADO, TRANSPORTE, etc.) agora são tratadas como strings normais.

---

## 🎯 Resumo

**Antes:** Limitado a 8 categorias fixas no enum  
**Agora:** Categorias ilimitadas e personalizadas!

✅ **Flexível**: Crie qualquer categoria que precisar  
✅ **Personalizado**: Nomes que façam sentido para você  
✅ **Isolado**: Suas categorias não afetam outros usuários  
✅ **Escalável**: Backend pronto para qualquer caso de uso

**Agora você tem total liberdade para organizar seus gastos do seu jeito!** 🎉

