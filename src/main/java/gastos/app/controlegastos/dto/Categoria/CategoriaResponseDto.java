package gastos.app.controlegastos.dto.Categoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDto {
    private UUID id;
    private String tipo;
    private LocalDate dtCriacao;
    private UsuarioBasicoDto usuario;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioBasicoDto {
        private UUID id;
        private String nome;
        private String email;
    }
}

