package gastos.app.controlegastos.service;

import gastos.app.controlegastos.dto.Usuario.UsuarioMapper;
import gastos.app.controlegastos.dto.Usuario.UsuarioRequestDto;
import gastos.app.controlegastos.dto.Usuario.UsuarioResponseDto;
import gastos.app.controlegastos.entity.Role;
import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.enums.RoleEnum;
import gastos.app.controlegastos.repository.RoleRepository;
import gastos.app.controlegastos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                         PasswordEncoder passwordEncoder,
                         RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UsuarioResponseDto salvar(UsuarioRequestDto request){
        Usuario usuario = UsuarioMapper.toEntity(request);

        // Encode password
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // Atribuir role ROLE_USER automaticamente
        Role roleUser = roleRepository.findByNome(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role ROLE_USER não encontrada. Execute o DataLoader."));

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        usuario.setRoles(roles);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponse(usuarioSalvo);
    }

    public List<UsuarioResponseDto> listarTodos(){
        return UsuarioMapper.toResponse(usuarioRepository.findAll());
    }

    public Optional<Usuario> buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorId(UUID id){
        return usuarioRepository.findById(id);
    }

    public UsuarioResponseDto atualizar(UUID id, UsuarioRequestDto dto){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        if (dto.getNome() != null) usuario.setNome(dto.getNome());
        if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponse(salvo);
    }

    public void deletar(UUID id){
        if (!usuarioRepository.existsById(id)){
            throw new RuntimeException("Usuário não encontrado com id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

}
