package gastos.app.controlegastos.config;

import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verifica se já existe algum usuário
            if (usuarioRepository.count() == 0) {
                // Cria um usuário padrão
                Usuario usuario = new Usuario();
                usuario.setNome("Admin");
                usuario.setEmail("joao@example.com");
                usuario.setSenha(passwordEncoder.encode("senha123"));

                usuarioRepository.save(usuario);

                System.out.println("✅ Usuário padrão criado:");
                System.out.println("   Email: joao@example.com");
                System.out.println("   Senha: senha123");
            } else {
                System.out.println("✅ Usuários já existem no banco de dados.");
            }
        };
    }
}

