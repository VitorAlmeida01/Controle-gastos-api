# 📊 Relacionamento: Usuario → Categorias

## ✅ Como Está Agora (CORRETO)

### Relacionamento no Banco de Dados

```
Usuario (1) ─── tem muitas ──→ (N) Categorias
```

**Um usuário tem várias categorias**  
**Uma categoria pertence a um único usuário**

---

## 🗂️ Estrutura das Entidades

### Usuario.java
```java
@Entity
public class Usuario {
    @Id
    private UUID id;
    private String nome;
    private String email;
    
    // Um usuário tem MUITAS categorias
    @OneToMany(mappedBy = "usuario")
    private List<Categoria> categorias = new ArrayList<>();
}
```

### Categoria.java
```java
@Entity
public class Categoria {
    @Id
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    private CategoriaEnum tipo;
    
    private LocalDate dtCriacao;
    
    // Uma categoria pertence a UM usuário
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
```

---

## 📤 Respostas da API

### POST /api/categorias (Cadastrar)

**Request:**
```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Mercado"}'
```

**Response (201 Created):**
```json
{
  "id": "40342540-3a95-4902-8dbc-a7d1b295f236",
  "tipo": "Mercado",
  "dtCriacao": "2026-03-14"
}
```

✅ **Não retorna dados do usuário** porque já está implícito que é do usuário logado

---

### GET /api/categorias (Listar)

**Request:**
```bash
curl -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer TOKEN"
```

**Response (200 OK):**
```json
[
  {
    "id": "40342540-3a95-4902-8dbc-a7d1b295f236",
    "tipo": "Mercado",
    "dtCriacao": "2026-03-14"
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "tipo": "Transporte",
    "dtCriacao": "2026-03-14"
  },
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "tipo": "Saúde",
    "dtCriacao": "2026-03-14"
  }
]
```

✅ **Lista todas as categorias do usuário logado**

---

## 🔄 Fluxo Completo

### 1. Login
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' \
  | jq -r '.token')
```

### 2. Cadastrar Categorias
```bash
# Cadastrar Mercado
curl -s -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Mercado"}' | jq '.'

# Cadastrar Transporte
curl -s -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Transporte"}' | jq '.'

# Cadastrar Saúde
curl -s -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"Saúde"}' | jq '.'
```

### 3. Listar Categorias do Usuário
```bash
curl -s -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resultado:**
```json
[
  {
    "id": "uuid-1",
    "tipo": "Mercado",
    "dtCriacao": "2026-03-14"
  },
  {
    "id": "uuid-2",
    "tipo": "Transporte",
    "dtCriacao": "2026-03-14"
  },
  {
    "id": "uuid-3",
    "tipo": "Saúde",
    "dtCriacao": "2026-03-14"
  }
]
```

---

## 🗄️ Estrutura no Banco de Dados

### Tabela: usuario
| id (UUID) | nome | email | senha |
|-----------|------|-------|-------|
| uuid-user-1 | Admin | admin@example.com | $2a$10... |
| uuid-user-2 | João | joao@example.com | $2a$10... |

### Tabela: categoria
| id (UUID) | tipo | dt_criacao | usuario_id (FK) |
|-----------|------|------------|-----------------|
| uuid-cat-1 | Mercado | 2026-03-14 | uuid-user-1 |
| uuid-cat-2 | Transporte | 2026-03-14 | uuid-user-1 |
| uuid-cat-3 | Saúde | 2026-03-14 | uuid-user-1 |
| uuid-cat-4 | Mercado | 2026-03-14 | uuid-user-2 |
| uuid-cat-5 | Alimentação | 2026-03-14 | uuid-user-2 |

✅ **Cada categoria tem um `usuario_id` (Foreign Key)**  
✅ **Cada usuário pode ter várias categorias**  
✅ **Categorias de diferentes usuários são isoladas**

---

## 👥 Isolamento de Dados

### Usuario 1 (Admin)
```bash
# Login como Admin
TOKEN_ADMIN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"admin123"}' | jq -r '.token')

# Listar categorias do Admin
curl -s -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN_ADMIN" | jq '.'
```

**Resultado:**
```json
[
  { "id": "uuid-1", "tipo": "Mercado", "dtCriacao": "2026-03-14" },
  { "id": "uuid-2", "tipo": "Transporte", "dtCriacao": "2026-03-14" }
]
```

---

### Usuario 2 (João)
```bash
# Login como João
TOKEN_JOAO=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@example.com","senha":"senha123"}' | jq -r '.token')

# Listar categorias do João
curl -s -X GET http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN_JOAO" | jq '.'
```

**Resultado:**
```json
[
  { "id": "uuid-3", "tipo": "Mercado", "dtCriacao": "2026-03-14" },
  { "id": "uuid-4", "tipo": "Alimentação", "dtCriacao": "2026-03-14" }
]
```

✅ **Cada usuário vê apenas suas próprias categorias**

---

## 🔐 Segurança

### Como o Sistema Garante o Isolamento?

```java
// 1. Extrai o email do token JWT
String email = authentication.getName();

// 2. Busca o usuário no banco
Usuario usuario = usuarioService.buscarPorEmail(email);

// 3. Ao cadastrar, associa automaticamente
categoria.setUsuario(usuario);

// 4. Ao listar, busca apenas do usuário logado
List<Categoria> categorias = usuario.getCategorias();
```

✅ **Não há como um usuário acessar categorias de outro**  
✅ **O token JWT garante a identidade**  
✅ **Cada operação verifica o usuário autenticado**

---

## 📊 Comparação: Antes vs Depois

### ❌ ANTES (Errado - retornava usuário na resposta)
```json
{
  "id": "40342540-3a95-4902-8dbc-a7d1b295f236",
  "tipo": "Mercado",
  "dtCriacao": "2026-03-14",
  "usuario": {
    "id": "c007fd82-d8aa-4436-af1b-5490c8bef21c",
    "nome": "João Silva",
    "email": "joao@example.com"
  }
}
```

**Problemas:**
- ❌ Redundante (já sabemos que é do usuário logado)
- ❌ Expõe dados desnecessários
- ❌ Response muito grande

---

### ✅ AGORA (Correto - limpo e direto)
```json
{
  "id": "40342540-3a95-4902-8dbc-a7d1b295f236",
  "tipo": "Mercado",
  "dtCriacao": "2026-03-14"
}
```

**Vantagens:**
- ✅ Response limpo e conciso
- ✅ Não expõe dados redundantes
- ✅ Menor tráfego de rede
- ✅ Mais seguro

---

## 🎯 Resumo

### Relacionamento:
```
Usuario (1) ──→ (N) Categorias
```

### No Banco:
- `categoria` tem campo `usuario_id` (Foreign Key)
- `usuario` **NÃO** tem lista de categorias na tabela (é uma relação virtual)

### Na Aplicação:
- `Usuario` tem `List<Categoria> categorias` (mapeado pelo JPA)
- `Categoria` tem `Usuario usuario` (dono da categoria)

### Nas Respostas:
- **Cadastrar categoria:** Retorna apenas dados da categoria
- **Listar categorias:** Retorna array de categorias do usuário logado
- **Não retorna dados do usuário** (já está implícito no token)

---

## 📚 Endpoints Disponíveis

### POST /api/categorias
✅ Cadastra categoria para o usuário logado

### GET /api/categorias
✅ Lista todas as categorias do usuário logado

### GET /api/categorias/{id}
✅ Busca uma categoria específica (se pertencer ao usuário)

### PUT /api/categorias/{id}
✅ Atualiza uma categoria (se pertencer ao usuário)

### DELETE /api/categorias/{id}
✅ Deleta uma categoria (se pertencer ao usuário)

---

## 💡 Dica

Se você precisar ver qual usuário está associado a uma categoria (para debug), pode fazer:

```bash
# No console H2
SELECT c.id, c.tipo, c.dt_criacao, c.usuario_id, u.nome, u.email
FROM categoria c
INNER JOIN usuario u ON c.usuario_id = u.id;
```

Mas na API, **não exponha o usuário** na resposta de categorias! 🔒

