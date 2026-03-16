# 👤 Atribuição Automática de Role ao Cadastrar Usuário

## ✅ O que foi implementado

Agora **todo usuário cadastrado recebe automaticamente a role `ROLE_USER`** sem necessidade de configuração manual.

---

## 🔄 Como Funciona

### Antes (Manual)
- ❌ Usuário criado sem role
- ❌ Precisava atribuir role manualmente
- ❌ Possível falha de segurança

### Agora (Automático)
- ✅ Usuário criado com `ROLE_USER` automaticamente
- ✅ Processo transparente para o usuário
- ✅ Segurança garantida desde o cadastro

---

## 💻 Implementação

### Código no `UsuarioService.java`

```java
public UsuarioResponseDto salvar(UsuarioRequestDto request){
    Usuario usuario = UsuarioMapper.toEntity(request);
    
    // Encode password
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    
    // Atribuir role ROLE_USER automaticamente
    Role roleUser = roleRepository.findByNome(RoleEnum.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Role ROLE_USER não encontrada"));
    
    Set<Role> roles = new HashSet<>();
    roles.add(roleUser);
    usuario.setRoles(roles);
    
    Usuario usuarioSalvo = usuarioRepository.save(usuario);
    return UsuarioMapper.toResponse(usuarioSalvo);
}
```

---

## 🚀 Testando

### 1. Cadastrar um Novo Usuário

```bash
curl -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste Usuario",
    "email": "teste@email.com",
    "senha": "senha123"
  }'
```

**Resposta:**
```json
{
  "id": "c007fd82-d8aa-4436-af1b-5490c8bef21c",
  "nome": "Teste Usuario",
  "email": "teste@email.com"
}
```

### 2. Fazer Login

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@email.com",
    "senha": "senha123"
  }' | jq -r '.token')

echo "Token: ${TOKEN:0:50}..."
```

### 3. Verificar Roles Atribuídas

```bash
curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

**Resposta:**
```json
{
  "email": "teste@email.com",
  "nome": "Teste Usuario",
  "roles": [
    "ROLE_USER"
  ]
}
```

✅ **Role `ROLE_USER` atribuída automaticamente!**

---

## 📊 Fluxo Completo

```bash
#!/bin/bash

echo "=== TESTE DE ATRIBUIÇÃO AUTOMÁTICA DE ROLE ==="
echo ""

# 1. Cadastrar usuário
echo "1. Cadastrando novo usuário..."
USUARIO=$(curl -s -X POST http://localhost:8080/api/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Teste",
    "email": "joao.teste@email.com",
    "senha": "teste123"
  }')

echo "$USUARIO" | jq '.'
echo ""

# 2. Fazer login
echo "2. Fazendo login..."
LOGIN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao.teste@email.com",
    "senha": "teste123"
  }')

TOKEN=$(echo "$LOGIN" | jq -r '.token')
echo "Token obtido: ${TOKEN:0:50}..."
echo ""

# 3. Verificar roles
echo "3. Verificando roles atribuídas..."
ROLES=$(curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN")

echo "$ROLES" | jq '.'
echo ""

# 4. Validar
ROLE_USER=$(echo "$ROLES" | jq -r '.roles[] | select(. == "ROLE_USER")')

if [ "$ROLE_USER" == "ROLE_USER" ]; then
  echo "✅ SUCESSO: Role ROLE_USER atribuída automaticamente!"
else
  echo "❌ ERRO: Role não foi atribuída corretamente"
fi
```

---

## 🔐 Segurança

### Roles Disponíveis:
- `ROLE_USER` - **Atribuído automaticamente** ao cadastrar
- `ROLE_ADMIN` - Deve ser atribuído manualmente (via banco ou script)

### Processo de Atribuição:
1. ✅ Usuário envia dados de cadastro
2. ✅ Sistema criptografa a senha
3. ✅ Sistema busca a role `ROLE_USER` no banco
4. ✅ Sistema atribui a role ao usuário
5. ✅ Sistema salva o usuário completo

---

## ⚠️ Requisitos

### Role `ROLE_USER` deve existir no banco

A role é criada automaticamente pelo `DataLoader.java` ao iniciar a aplicação:

```java
@Bean
CommandLineRunner initDatabase(...) {
    return args -> {
        // Criar role ROLE_USER se não existir
        Role roleUser = roleRepository.findByNome(RoleEnum.ROLE_USER)
                .orElseGet(() -> {
                    Role role = new Role(RoleEnum.ROLE_USER);
                    return roleRepository.save(role);
                });
        
        // ...
    };
}
```

### Se a role não existir:
```
RuntimeException: Role ROLE_USER não encontrada. Execute o DataLoader.
```

**Solução:** Reinicie a aplicação para que o `DataLoader` crie as roles.

---

## 👑 Como Tornar um Usuário Admin

Para atribuir `ROLE_ADMIN` a um usuário existente, você precisa fazer manualmente via SQL ou criar um endpoint específico:

### Opção 1: Via SQL (H2 Console)

```sql
-- 1. Buscar ID do usuário
SELECT id, nome, email FROM usuario WHERE email = 'teste@email.com';

-- 2. Buscar ID da role ROLE_ADMIN
SELECT id, nome FROM role WHERE nome = 'ROLE_ADMIN';

-- 3. Inserir na tabela intermediária
INSERT INTO usuario_roles (usuario_id, role_id) 
VALUES ('uuid-do-usuario', 'uuid-do-role-admin');
```

### Opção 2: Via Código (DataLoader)

No `DataLoader.java`, você pode adicionar:

```java
// Buscar usuário específico
Usuario usuario = usuarioRepository.findByEmail("teste@email.com")
        .orElse(null);

if (usuario != null) {
    // Adicionar role ADMIN
    Role roleAdmin = roleRepository.findByNome(RoleEnum.ROLE_ADMIN)
            .orElseThrow();
    
    usuario.getRoles().add(roleAdmin);
    usuarioRepository.save(usuario);
    
    System.out.println("✅ Role ADMIN adicionada ao usuário: " + usuario.getEmail());
}
```

---

## 📋 Comparação

### Usuário Comum (ROLE_USER)
```json
{
  "email": "usuario@email.com",
  "nome": "Usuário Comum",
  "roles": [
    "ROLE_USER"
  ]
}
```

**Permissões:**
- ✅ Criar categorias
- ✅ Criar gastos
- ✅ Ver próprios gastos
- ✅ Atualizar próprios dados
- ❌ Acessar dados de outros usuários
- ❌ Funções administrativas

---

### Usuário Admin (ROLE_ADMIN + ROLE_USER)
```json
{
  "email": "admin@example.com",
  "nome": "Admin",
  "roles": [
    "ROLE_ADMIN",
    "ROLE_USER"
  ]
}
```

**Permissões:**
- ✅ Tudo que ROLE_USER pode fazer
- ✅ Acessar dados de todos os usuários (se implementado)
- ✅ Funções administrativas (se implementado)

---

## 🎯 Vantagens da Atribuição Automática

### 1. Segurança
- ✅ Todos os usuários têm permissões básicas
- ✅ Não há usuários sem role
- ✅ Sistema consistente

### 2. Simplicidade
- ✅ Frontend não precisa enviar role
- ✅ Backend gerencia automaticamente
- ✅ Menos chance de erro

### 3. Escalabilidade
- ✅ Fácil adicionar mais roles no futuro
- ✅ Lógica centralizada
- ✅ Manutenção simplificada

---

## 📝 Body do Cadastro

### O que enviar:
```json
{
  "nome": "Nome Completo",
  "email": "email@example.com",
  "senha": "senha_segura"
}
```

### O que NÃO precisa enviar:
- ❌ `roles` - Atribuído automaticamente
- ❌ `id` - Gerado automaticamente

---

## 🔄 Processo Completo

```
1. Usuário envia:
   POST /api/usuarios/register
   Body: { nome, email, senha }

2. Backend:
   - Valida dados
   - Criptografa senha
   - Busca ROLE_USER
   - Atribui role ao usuário
   - Salva no banco

3. Banco de Dados:
   - Tabela usuario (id, nome, email, senha_hash)
   - Tabela usuario_roles (usuario_id, role_id)
   - Relacionamento automático

4. Resposta:
   { id, nome, email }
   (sem senha, sem roles na resposta)

5. Login:
   Token JWT contém as roles
   { email, nome, roles: ["ROLE_USER"] }
```

---

## 🎓 Resumo

**O que mudou:**
- ✅ Todo novo usuário recebe `ROLE_USER` automaticamente
- ✅ Não precisa configurar manualmente
- ✅ Sistema mais seguro e consistente

**Como testar:**
1. Cadastre um usuário com `/api/usuarios/register`
2. Faça login com `/api/auth/login`
3. Verifique as roles com `/api/auth/me`
4. Confirme que `ROLE_USER` está presente

**Requisito:**
- ✅ Role `ROLE_USER` deve existir no banco (criada pelo DataLoader)

**Agora todo usuário tem permissões básicas desde o cadastro!** 👤✅

