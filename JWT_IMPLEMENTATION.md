# 🎯 Login JWT Implementado com Sucesso!

## ✅ O que foi implementado

A API ControleGastos agora possui um sistema completo de autenticação JWT (JSON Web Token).

### 🔐 Endpoints de Autenticação

| Endpoint | Método | Descrição |
|----------|--------|-----------|
| `POST /api/auth/login` | POST | Fazer login e receber token JWT |
| `GET /api/auth/validate` | GET | Validar se um token é válido |
| `POST /api/usuarios/register` | POST | Registrar novo usuário (público) |

---

## 🚀 Como Usar (3 Passos Rápidos)

### 1. Registrar Usuário
```bash
curl -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"João","email":"joao@test.com","senha":"123456"}'
```

### 2. Fazer Login e Obter Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@test.com","senha":"123456"}'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvQHRlc3QuY29tIi...",
  "tipo": "Bearer",
  "email": "joao@test.com",
  "nome": "João"
}
```

### 3. Usar Token nas Requisições
```bash
curl -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  http://localhost:8080/api/gastos
```

---

## 📁 Arquivos Criados/Modificados

### Novos Arquivos Java:
- ✅ `JwtUtil.java` - Utilitário para gerar e validar tokens JWT
- ✅ `JwtAuthenticationFilter.java` - Filtro que intercepta requisições e valida token
- ✅ `LoginRequest.java` - DTO para requisição de login
- ✅ `LoginResponse.java` - DTO para resposta de login com token
- ✅ `AuthController.java` - Controller com endpoints de login e validação

### Arquivos Modificados:
- ✅ `SecurityConfig.java` - Atualizado para suportar JWT
- ✅ `pom.xml` - Adicionadas dependências JWT (jjwt 0.12.3)

### Documentação:
- ✅ `JWT_LOGIN_GUIDE.md` - Guia completo de uso do JWT
- ✅ `CURL_COMMANDS.md` - Atualizado com comandos de login JWT
- ✅ `test-jwt.sh` - Script de teste automatizado
- ✅ `ControleGastos.postman_collection.json` - Collection atualizada

---

## 🔧 Configuração JWT

### application.properties (ou application-jwt.properties)
```properties
jwt.secret=minha-chave-secreta-muito-segura-e-longa-para-hmac-sha256-precisa-ter-pelo-menos-256-bits
jwt.expiration=86400000
```

**Importante em produção:**
- Mude a `jwt.secret` para uma chave única e segura
- A expiração atual é de 24 horas (86400000 ms)

---

## 🎮 Como Testar

### Opção 1: Script Automático
```bash
chmod +x test-jwt.sh
./test-jwt.sh
```

### Opção 2: Comandos Manuais
Veja o arquivo `JWT_LOGIN_GUIDE.md` para exemplos completos.

### Opção 3: Postman/Insomnia
Importe o arquivo `ControleGastos.postman_collection.json`

---

## 🔑 Estrutura do Token JWT

### Header
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

### Payload
```json
{
  "sub": "email@example.com",
  "iat": 1707067200,
  "exp": 1707153600
}
```

### Signature
Assinado com HS256 usando a chave secreta configurada.

---

## 📊 Fluxo de Autenticação

```
1. Cliente → POST /api/usuarios/register → Servidor
   ↓
2. Servidor cria usuário (senha criptografada com BCrypt)
   ↓
3. Cliente → POST /api/auth/login → Servidor
   ↓
4. Servidor valida credenciais
   ↓
5. Servidor gera token JWT assinado
   ↓
6. Cliente recebe token
   ↓
7. Cliente → GET /api/gastos + Header: "Authorization: Bearer TOKEN" → Servidor
   ↓
8. JwtAuthenticationFilter valida token
   ↓
9. Servidor retorna dados (se token válido)
```

---

## 🛡️ Segurança Implementada

✅ **Senhas criptografadas** com BCrypt  
✅ **Tokens assinados** com HS256  
✅ **Validação automática** de tokens em cada requisição  
✅ **Expiração de tokens** (24h)  
✅ **Stateless** - não armazena sessão no servidor  
✅ **CORS configurado** para http://localhost:4200  
✅ **Endpoints públicos**: /register e /login  
✅ **Endpoints protegidos**: todos os demais  

---

## 🔍 Endpoints Protegidos vs Públicos

### 🔓 Públicos (não requerem token):
- `POST /api/usuarios/register`
- `POST /api/auth/login`
- `GET /api/auth/validate`
- `GET /h2-console/**`

### 🔐 Protegidos (requerem token JWT):
- `GET /api/usuarios`
- `GET /api/gastos`
- `POST /api/gastos`
- `PUT /api/gastos/{id}`
- `DELETE /api/gastos/{id}`
- `GET /api/gastos/total`
- `GET /api/gastos/total/tipo/{tipo}`
- Todos os demais endpoints da API

---

## ⚙️ Dependências Adicionadas

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

---

## 🚦 Como Iniciar a Aplicação

```bash
# Compilar e baixar dependências
./mvnw clean install

# Iniciar aplicação
./mvnw spring-boot:run
```

Aguarde a mensagem: `Started ControleGastosApplication`

---

## 📚 Documentação Adicional

- **JWT_LOGIN_GUIDE.md** - Guia completo com exemplos de uso
- **CURL_COMMANDS.md** - Todos os comandos cURL disponíveis
- **QUICK_START.md** - Guia rápido para começar
- **test-jwt.sh** - Script de teste automatizado

---

## 🐛 Troubleshooting

### Erro: "Cannot resolve symbol 'jsonwebtoken'"
**Solução:** Execute `./mvnw clean install` para baixar dependências

### Erro 401: "Token inválido ou expirado"
**Solução:** Faça login novamente para obter um novo token

### Erro: "Email ou senha inválidos"
**Solução:** Verifique as credenciais ou registre um novo usuário

### Token muito longo no terminal
**Solução:** Use variável de ambiente:
```bash
export TOKEN="seu_token"
curl -H "Authorization: Bearer $TOKEN" URL
```

---

## 🎉 Próximos Passos

1. ✅ JWT implementado e funcionando
2. 🔄 Teste todos os endpoints com `./test-jwt.sh`
3. 📱 Integre com seu frontend (Angular, React, etc.)
4. 🔒 Em produção: use HTTPS e mude a chave secreta
5. 🚀 Opcional: Implemente refresh token se necessário

---

## 💡 Exemplo de Integração Frontend

### JavaScript/Fetch
```javascript
// Login
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email: 'user@test.com', senha: '123456' })
});
const { token } = await response.json();

// Salvar token
localStorage.setItem('token', token);

// Usar em requisições
const gastos = await fetch('http://localhost:8080/api/gastos', {
  headers: { 'Authorization': `Bearer ${token}` }
});
```

### Angular
```typescript
// auth.service.ts
login(email: string, senha: string) {
  return this.http.post('/api/auth/login', { email, senha })
    .pipe(tap((response: any) => {
      localStorage.setItem('token', response.token);
    }));
}

// http.interceptor.ts
intercept(req: HttpRequest<any>, next: HttpHandler) {
  const token = localStorage.getItem('token');
  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }
  return next.handle(req);
}
```

---

**✅ JWT implementado com sucesso!**

🎊 Parabéns! Sua API agora possui autenticação moderna e segura com JWT.

Para dúvidas ou mais exemplos, consulte os arquivos de documentação criados.

