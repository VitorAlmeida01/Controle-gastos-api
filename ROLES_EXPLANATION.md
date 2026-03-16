# 🔐 Roles (Papéis) no Sistema Atual

## 📊 Estado Atual

**Resposta curta:** ❌ **NÃO**, o sistema atual **NÃO possui um sistema completo de roles**.

### O que existe atualmente:

1. **Role Padrão Hardcoded:**
   - Todos os usuários recebem automaticamente a role `ROLE_USER`
   - Definido em: `UsuarioDetailsService.java`, linha 28

```java
// Código atual em UsuarioDetailsService.java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    // for simplicity, everyone gets ROLE_USER
    return new User(usuario.getEmail(), usuario.getSenha(), 
                   List.of(new SimpleGrantedAuthority("ROLE_USER")));
}
```

2. **Sem Campo no Banco de Dados:**
   - A entidade `Usuario` **NÃO possui** um campo para armazenar roles
   - Não existe tabela ou enum para gerenciar papéis

3. **Sem Controle de Acesso por Role:**
   - Não há anotações `@PreAuthorize` ou `@RolesAllowed` nos controllers
   - Todos os endpoints autenticados têm o mesmo nível de acesso
   - A configuração de segurança só verifica se o usuário está autenticado

---

## 🔍 Análise da Configuração Atual

### SecurityConfig.java
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/api/usuarios/register").permitAll()
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/auth/validate").permitAll()
            .anyRequest().authenticated()  // ⚠️ Apenas verifica autenticação, não role
        )
        // ...
}
```

**Observação:** O `.anyRequest().authenticated()` apenas verifica se o usuário está logado, **não** verifica sua role.

---

## ❓ Por que não afeta o sistema atual?

Como todos os endpoints (exceto os públicos) requerem apenas autenticação, o sistema funciona assim:

- ✅ Usuário autenticado → Pode acessar qualquer endpoint
- ❌ Usuário não autenticado → Bloqueado

**Não há diferenciação entre tipos de usuários** (admin, user comum, etc.)

---

## 🚀 Se você precisar implementar Roles

### Opção 1: Sistema Simples com Enum

#### 1. Criar o Enum de Roles
```java
// src/main/java/gastos/app/controlegastos/enums/RoleEnum.java
package gastos.app.controlegastos.enums;

public enum RoleEnum {
    ROLE_USER,
    ROLE_ADMIN
}
```

#### 2. Adicionar Role na Entidade Usuario
```java
@Entity
public class Usuario {
    // ...campos existentes...
    
    @Enumerated(EnumType.STRING)
    private RoleEnum role = RoleEnum.ROLE_USER; // Padrão
}
```

#### 3. Atualizar UsuarioDetailsService
```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    
    return new User(
        usuario.getEmail(), 
        usuario.getSenha(), 
        List.of(new SimpleGrantedAuthority(usuario.getRole().name()))
    );
}
```

#### 4. Proteger Endpoints com Roles
```java
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Somente admins
    public ResponseEntity<List<UsuarioResponseDto>> listar() {
        // ...
    }
}
```

---

### Opção 2: Sistema Avançado com Múltiplas Roles

Para cenários mais complexos onde um usuário pode ter múltiplas roles:

#### 1. Criar Entidade Role
```java
@Entity
public class Role {
    @Id
    @GeneratedValue
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    private RoleEnum nome;
}
```

#### 2. Relacionamento Many-to-Many
```java
@Entity
public class Usuario {
    // ...campos existentes...
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_role",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
```

---

## 📝 Exemplo de Uso com Roles

### Diferentes Níveis de Acesso

```java
// ✅ Qualquer usuário autenticado
@GetMapping("/meus-gastos")
public ResponseEntity<?> meusgastos() { ... }

// ✅ Apenas usuários com ROLE_USER ou superior
@GetMapping("/relatorio")
@PreAuthorize("hasRole('USER')")
public ResponseEntity<?> relatorio() { ... }

// ✅ Apenas administradores
@DeleteMapping("/usuarios/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> deletarUsuario(@PathVariable UUID id) { ... }

// ✅ Admin OU o próprio usuário
@PutMapping("/usuarios/{id}")
@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
public ResponseEntity<?> atualizar(@PathVariable UUID id) { ... }
```

---

## 🎯 Conclusão

### Estado Atual:
- ❌ Sem sistema de roles persistido
- ❌ Sem diferenciação de permissões
- ✅ Apenas autenticação básica (logado/não logado)
- ✅ Todos os usuários autenticados têm acesso total

### Se você precisa de roles:
1. Decida o nível de complexidade (simples enum ou múltiplas roles)
2. Adicione o campo na entidade Usuario
3. Atualize o UsuarioDetailsService
4. Use `@PreAuthorize` nos endpoints que precisam de proteção específica

### Se NÃO precisa de roles:
O sistema atual está funcionando perfeitamente para um cenário onde todos os usuários autenticados têm os mesmos privilégios.

---

## 📚 Documentação Relacionada

- [JWT Login Guide](./JWT_LOGIN_GUIDE.md)
- [JWT Implementation](./JWT_IMPLEMENTATION.md)
- [Security Config](./src/main/java/gastos/app/controlegastos/config/SecurityConfig.java)
- [Usuario Details Service](./src/main/java/gastos/app/controlegastos/service/UsuarioDetailsService.java)

