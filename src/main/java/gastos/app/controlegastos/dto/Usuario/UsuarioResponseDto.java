package gastos.app.controlegastos.dto.Usuario;

import java.util.UUID;

public record UsuarioResponseDto (
        UUID id,
        String nome,
        String email
) {}
