package gastos.app.controlegastos.dto.Gasto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GastoRequestDto {

    private UUID categoriaId;
    private Double valor;
}

