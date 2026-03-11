package gastos.app.controlegastos.dto.Gasto;


import gastos.app.controlegastos.enums.CategoriaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GastoRequestDto {

    private CategoriaEnum tipo;
    private Double valor;
}

