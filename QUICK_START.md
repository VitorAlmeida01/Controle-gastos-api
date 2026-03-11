# 🧪 Guia de Testes da API - Quick Start

## 🚀 Como Iniciar

### 1. Iniciar a aplicação
```bash
./mvnw spring-boot:run
```

Aguarde até ver: `Started ControleGastosApplication`

### 2. Escolha seu método de teste

#### Opção A: Comandos cURL individuais
Abra o arquivo `CURL_COMMANDS.md` para ver todos os comandos disponíveis.

#### Opção B: Script automatizado
```bash
chmod +x test-api.sh
./test-api.sh
```

#### Opção C: Postman/Insomnia
Importe o arquivo `ControleGastos.postman_collection.json`

---

## ⚡ Teste Rápido (Copy & Paste)

### Passo 1: Registrar Usuário
```bash
curl -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste","email":"teste@email.com","senha":"123456"}'
```

### Passo 2: Criar Categoria no H2
1. Acesse: http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:testdb`
3. User: `sa`, Password: (vazio)
4. Execute:
```sql
INSERT INTO CATEGORIA (ID, TIPO, DT_CRIACAO, USUARIO_ID) 
VALUES (RANDOM_UUID(), 'MERCADO', CURRENT_DATE, 
  (SELECT ID FROM USUARIO WHERE EMAIL = 'teste@email.com'));
```

### Passo 3: Criar Gasto
```bash
# Substitua UUID_DA_CATEGORIA pelo ID retornado no H2
curl -X POST http://localhost:8080/api/gastos \
  -u teste@email.com:123456 \
  -H "Content-Type: application/json" \
  -d '{"valor":100.50,"categoria":{"id":"UUID_DA_CATEGORIA"}}'
```

### Passo 4: Ver Total
```bash
curl -u teste@email.com:123456 \
  http://localhost:8080/api/gastos/total
```

---

## 📋 Endpoints Principais

| Método | Endpoint | Auth? | Descrição |
|--------|----------|-------|-----------|
| POST | `/api/usuarios/register` | ❌ | Registrar novo usuário |
| GET | `/api/usuarios` | ✅ | Listar usuários |
| POST | `/api/gastos` | ✅ | Criar gasto |
| GET | `/api/gastos` | ✅ | Listar gastos |
| GET | `/api/gastos/{id}` | ✅ | Buscar gasto por ID |
| GET | `/api/gastos/tipo/{tipo}` | ✅ | Buscar por tipo |
| PUT | `/api/gastos/{id}` | ✅ | Atualizar gasto |
| DELETE | `/api/gastos/{id}` | ✅ | Deletar gasto |
| GET | `/api/gastos/total` | ✅ | Total geral |
| GET | `/api/gastos/total/tipo/{tipo}` | ✅ | Total por tipo |

---

## 🔑 Autenticação

Todos os endpoints (exceto `/register`) exigem HTTP Basic Auth:

```bash
-u email:senha
```

**Ou com header:**
```bash
-H "Authorization: Basic $(echo -n 'email:senha' | base64)"
```

---

## 🎯 Tipos de Categoria Disponíveis

- `MERCADO`
- `TRANSPORTE`
- `ALIMENTACAO`
- `SAUDE`
- `LAZER`
- `EDUCACAO`
- `MORADIA`
- `OUTROS`

---

## 🐛 Resolução de Problemas

### Erro: Connection refused
✅ Verifique se a aplicação está rodando em http://localhost:8080

### Erro: 401 Unauthorized
✅ Verifique email e senha  
✅ Certifique-se de usar `-u email:senha`

### Erro: 404 Not Found
✅ Confirme o UUID correto do recurso  
✅ Verifique se o endpoint está correto

### Erro: Table USUARIO not found
✅ Reinicie a aplicação (banco H2 em memória)  
✅ Verifique se o JPA está gerando as tabelas

---

## 📁 Arquivos Úteis

- `CURL_COMMANDS.md` - Todos os comandos cURL detalhados
- `test-api.sh` - Script de teste automatizado
- `ControleGastos.postman_collection.json` - Collection para Postman
- Este arquivo - Guia rápido

---

## 💡 Dicas

1. **Use jq para formatar JSON:**
   ```bash
   curl ... | jq '.'
   ```

2. **Salve IDs em variáveis:**
   ```bash
   USER_ID=$(curl ... | jq -r '.id')
   ```

3. **Ver headers completos:**
   ```bash
   curl -i ...
   ```

4. **Debug detalhado:**
   ```bash
   curl -v ...
   ```

---

**🎉 Pronto! Agora você pode testar toda a API.**

Para mais detalhes, consulte `CURL_COMMANDS.md`

