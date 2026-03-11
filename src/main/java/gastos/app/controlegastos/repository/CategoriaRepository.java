package gastos.app.controlegastos.repository;

import gastos.app.controlegastos.entity.Categoria;
import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.enums.CategoriaEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    Optional<Categoria> findByUsuarioAndTipo(Usuario usuario, CategoriaEnum tipo);
}
