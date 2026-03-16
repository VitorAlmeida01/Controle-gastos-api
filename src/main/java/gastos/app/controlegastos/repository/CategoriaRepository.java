package gastos.app.controlegastos.repository;

import gastos.app.controlegastos.entity.Categoria;
import gastos.app.controlegastos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    Optional<Categoria> findByUsuarioAndTipo(Usuario usuario, String tipo);
}
