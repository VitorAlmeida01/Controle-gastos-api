package gastos.app.controlegastos.entity;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Double valor;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;



}




