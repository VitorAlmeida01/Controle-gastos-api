package gastos.app.controlegastos.config;

import gastos.app.controlegastos.entity.Role;
import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.enums.RoleEnum;
import gastos.app.controlegastos.repository.RoleRepository;
import gastos.app.controlegastos.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Criar roles se não existirem
            Role roleUser = roleRepository.findByNome(RoleEnum.ROLE_USER)
                    .orElseGet(() -> {
                        Role role = new Role(RoleEnum.ROLE_USER);
                        return roleRepository.save(role);
                    });

            Role roleAdmin = roleRepository.findByNome(RoleEnum.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role role = new Role(RoleEnum.ROLE_ADMIN);
                        return roleRepository.save(role);
                    });

            System.out.println("✅ Roles criadas/verificadas:");
            System.out.println("   - ROLE_USER");
            System.out.println("   - ROLE_ADMIN");

            // 2. Criar usuário padrão se não existir
            if (usuarioRepository.count() == 0) {
                // Criar usuário admin
                Usuario admin = new Usuario();
                admin.setNome("Admin");
                admin.setEmail("admin@example.com");
                admin.setSenha(passwordEncoder.encode("admin123"));

                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(roleAdmin);
                adminRoles.add(roleUser);
                admin.setRoles(adminRoles);

                usuarioRepository.save(admin);

                // Criar usuário comum
                Usuario user = new Usuario();
                user.setNome("João Silva");
                user.setEmail("joao@example.com");
                user.setSenha(passwordEncoder.encode("senha123"));

                Set<Role> userRoles = new HashSet<>();
                userRoles.add(roleUser);
                user.setRoles(userRoles);

                usuarioRepository.save(user);

                System.out.println("\n✅ Usuários padrão criados:");
                System.out.println("\n   👑 ADMIN:");
                System.out.println("      Email: admin@example.com");
                System.out.println("      Senha: admin123");
                System.out.println("      Roles: ROLE_ADMIN, ROLE_USER");
                System.out.println("\n   👤 USER:");
                System.out.println("      Email: joao@example.com");
                System.out.println("      Senha: senha123");
                System.out.println("      Roles: ROLE_USER");
            } else {
                System.out.println("✅ Usuários já existem no banco de dados.");
            }
        };
    }
}

