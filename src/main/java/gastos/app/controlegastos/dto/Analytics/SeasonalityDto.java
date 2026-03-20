package gastos.app.controlegastos.dto.Analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonalityDto {
    private String periodo;
    private Double total;
    private Long quantidade;
}

