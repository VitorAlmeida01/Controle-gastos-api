package gastos.app.controlegastos.service;

import gastos.app.controlegastos.dto.Usuario.UsuarioMapper;
import gastos.app.controlegastos.dto.Usuario.UsuarioRequestDto;
import gastos.app.controlegastos.dto.Usuario.UsuarioResponseDto;
import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDto salvar(UsuarioRequestDto request){
        Usuario usuario = UsuarioMapper.toEntity(request);
        // encode password
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
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

}
