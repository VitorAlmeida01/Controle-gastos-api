package gastos.app.controlegastos.dto.Analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvgCategoriaDto {
    private String tipo;
    private Double media;
}

