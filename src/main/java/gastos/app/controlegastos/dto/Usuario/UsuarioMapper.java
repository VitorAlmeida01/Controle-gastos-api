package gastos.app.controlegastos.dto.Usuario;

import gastos.app.controlegastos.entity.Usuario;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {
    public static Usuario toEntity(UsuarioRequestDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        return usuario;
    }

    public static UsuarioResponseDto toResponse(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    public static List<UsuarioResponseDto> toResponse(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static UsuarioResponseDto[] toResponseArray(Usuario[] usuarios){
        return Arrays.stream(usuarios)
                .map(UsuarioMapper::toResponse)
                .toArray(UsuarioResponseDto[]::new);
    }

    public static Usuario[] toEntityArray(UsuarioRequestDto[] dtos){
        return Arrays.stream(dtos)
                .map(UsuarioMapper::toEntity)
                .toArray(Usuario[]::new);
    }
}
