package gastos.app.controlegastos.dto.Categoria;

import gastos.app.controlegastos.enums.CategoriaEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDto {
    @Enumerated(EnumType.STRING)
    private CategoriaEnum tipo;
}
