package gastos.app.controlegastos.dto.Gasto;

import gastos.app.controlegastos.enums.CategoriaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GastoResponseDto {
    private UUID id;
    private Double valor;
    private CategoriaEnum tipo;
}

