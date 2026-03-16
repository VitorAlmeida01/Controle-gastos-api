package gastos.app.controlegastos.repository;

import gastos.app.controlegastos.entity.Role;
import gastos.app.controlegastos.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByNome(RoleEnum nome);
}

