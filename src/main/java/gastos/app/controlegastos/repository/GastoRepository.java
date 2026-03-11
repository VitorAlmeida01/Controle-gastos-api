package gastos.app.controlegastos.repository;

import gastos.app.controlegastos.entity.Gasto;
import gastos.app.controlegastos.enums.CategoriaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, UUID> {

    List<Gasto> findByCategoria_Tipo(CategoriaEnum tipo);
}
