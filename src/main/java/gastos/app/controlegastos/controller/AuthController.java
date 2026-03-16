package gastos.app.controlegastos.controller;

import gastos.app.controlegastos.dto.Auth.LoginRequest;
import gastos.app.controlegastos.dto.Auth.LoginResponse;
import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.security.JwtUtil;
import gastos.app.controlegastos.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
    @RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Autenticar usuário
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getSenha()
                    )
            );

            // Carregar detalhes do usuário
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Buscar dados completos do usuário
            Usuario usuario = usuarioService.buscarPorEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Gerar token JWT incluindo ID, nome, email e roles nas claims
            final String jwt = jwtUtil.generateToken(
                userDetails,
                usuario.getId().toString(),
                usuario.getNome(),
                usuario.getEmail()
            );

            // Criar resposta
            LoginResponse response = new LoginResponse(jwt, usuario.getEmail(), usuario.getNome());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou senha inválidos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao realizar login: " + e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails)) {
                    return ResponseEntity.ok("Token válido");
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou expirado");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getTokenInfo(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // Extrair informações do token
                String id = jwtUtil.extractId(token);
                String email = jwtUtil.extractEmail(token);
                String nome = jwtUtil.extractNome(token);
                java.util.List<String> roles = jwtUtil.extractRoles(token);

                // Criar resposta com as informações
                java.util.Map<String, Object> userInfo = new java.util.HashMap<>();
                userInfo.put("id", id);
                userInfo.put("email", email);
                userInfo.put("nome", nome);
                userInfo.put("roles", roles);

                return ResponseEntity.ok(userInfo);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token não fornecido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro ao extrair informações do token: " + e.getMessage());
        }
    }
}

