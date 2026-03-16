package gastos.app.controlegastos.dto.Gasto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GastoResponseDto {
    private UUID id;
    private Double valor;
    private String tipo;
    private LocalDateTime dtCriacao;
    private String descricao;
}
