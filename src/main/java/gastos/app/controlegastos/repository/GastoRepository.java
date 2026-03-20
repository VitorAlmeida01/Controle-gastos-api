package gastos.app.controlegastos.repository;

import gastos.app.controlegastos.entity.Gasto;
import gastos.app.controlegastos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, UUID> {

    // Buscar gastos por tipo de categoria
    List<Gasto> findByCategoria_Tipo(String tipo);

    // Buscar todos os gastos de um usuário
    List<Gasto> findByUsuario(Usuario usuario);

    // Buscar gastos de um usuário por ID
    List<Gasto> findByUsuario_Id(UUID usuarioId);

    // Buscar gastos de um usuário por tipo de categoria
    List<Gasto> findByUsuario_IdAndCategoria_Tipo(UUID usuarioId, String tipo);

    // Buscar gastos por período
    @Query("SELECT g FROM Gasto g WHERE g.dtCriacao BETWEEN :inicio AND :fim ORDER BY g.dtCriacao DESC")
    List<Gasto> findByDtCriacaoBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    // Buscar gastos por período e usuário (CORRIGIDO para usar g.usuario.id)
    @Query("SELECT g FROM Gasto g WHERE g.usuario.id = :usuarioId AND g.dtCriacao BETWEEN :inicio AND :fim ORDER BY g.dtCriacao DESC")
    List<Gasto> findByUsuarioAndPeriodo(@Param("usuarioId") UUID usuarioId, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    // Media por categoria (todos os usuarios)
    @Query("SELECT g.categoria.tipo, AVG(g.valor) FROM Gasto g GROUP BY g.categoria.tipo")
    List<Object[]> avgValorPorCategoria();

    // Media por categoria (usuario)
    @Query("SELECT g.categoria.tipo, AVG(g.valor) FROM Gasto g WHERE g.usuario.id = :usuarioId GROUP BY g.categoria.tipo")
    List<Object[]> avgValorPorCategoriaUsuario(@Param("usuarioId") UUID usuarioId);

    // Ticket medio (todos os usuarios)
    @Query("SELECT AVG(g.valor) FROM Gasto g")
    Double avgTicket();

    // Ticket medio (usuario)
    @Query("SELECT AVG(g.valor) FROM Gasto g WHERE g.usuario.id = :usuarioId")
    Double avgTicketUsuario(@Param("usuarioId") UUID usuarioId);

    // Sazonalidade por mes (todos os usuarios)
    @Query("SELECT FUNCTION('YEAR', g.dtCriacao), FUNCTION('MONTH', g.dtCriacao), SUM(g.valor), COUNT(g) " +
            "FROM Gasto g " +
            "GROUP BY FUNCTION('YEAR', g.dtCriacao), FUNCTION('MONTH', g.dtCriacao) " +
            "ORDER BY FUNCTION('YEAR', g.dtCriacao), FUNCTION('MONTH', g.dtCriacao)")
    List<Object[]> seasonalityByMonth();

    // Sazonalidade por mes (usuario)
    @Query("SELECT FUNCTION('YEAR', g.dtCriacao), FUNCTION('MONTH', g.dtCriacao), SUM(g.valor), COUNT(g) " +
            "FROM Gasto g WHERE g.usuario.id = :usuarioId " +
            "GROUP BY FUNCTION('YEAR', g.dtCriacao), FUNCTION('MONTH', g.dtCriacao) " +
            "ORDER BY FUNCTION('YEAR', g.dtCriacao), FUNCTION('MONTH', g.dtCriacao)")
    List<Object[]> seasonalityByMonthUsuario(@Param("usuarioId") UUID usuarioId);

    // Sazonalidade por dia da semana (todos os usuarios)
    @Query("SELECT FUNCTION('DAY_OF_WEEK', g.dtCriacao), SUM(g.valor), COUNT(g) " +
            "FROM Gasto g " +
            "GROUP BY FUNCTION('DAY_OF_WEEK', g.dtCriacao) " +
            "ORDER BY FUNCTION('DAY_OF_WEEK', g.dtCriacao)")
    List<Object[]> seasonalityByDayOfWeek();

    // Sazonalidade por dia da semana (usuario)
    @Query("SELECT FUNCTION('DAY_OF_WEEK', g.dtCriacao), SUM(g.valor), COUNT(g) " +
            "FROM Gasto g " +
            "WHERE g.usuario.id = :usuarioId " +
            "GROUP BY FUNCTION('DAY_OF_WEEK', g.dtCriacao) " +
            "ORDER BY FUNCTION('DAY_OF_WEEK', g.dtCriacao)")
    List<Object[]> seasonalityByDayOfWeekUsuario(@Param("usuarioId") UUID usuarioId);
}
