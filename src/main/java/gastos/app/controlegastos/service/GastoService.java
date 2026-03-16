package gastos.app.controlegastos.service;

import gastos.app.controlegastos.entity.Gasto;
import gastos.app.controlegastos.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;

    // Listar todos os gastos (ADMIN - não filtrado)
    public List<Gasto> listarTodos() {
        return gastoRepository.findAll();
    }

    // Listar gastos de um usuário específico
    public List<Gasto> listarPorUsuario(UUID usuarioId) {
        return gastoRepository.findByUsuario_Id(usuarioId);
    }

    public Optional<Gasto> buscarPorId(UUID id) {
        return gastoRepository.findById(id);
    }

    // Buscar gasto por ID e validar se pertence ao usuário
    public Optional<Gasto> buscarPorIdEUsuario(UUID gastoId, UUID usuarioId) {
        return gastoRepository.findById(gastoId)
                .filter(gasto -> gasto.getUsuario().getId().equals(usuarioId));
    }

    public List<Gasto> buscarPorTipo(String tipo) {
        return gastoRepository.findByCategoria_Tipo(tipo);
    }

    // Buscar por tipo filtrado por usuário
    public List<Gasto> buscarPorTipoEUsuario(String tipo, UUID usuarioId) {
        return gastoRepository.findByUsuario_IdAndCategoria_Tipo(usuarioId, tipo);
    }

    public Gasto salvar(Gasto gasto) {
        return gastoRepository.save(gasto);
    }

    public Gasto atualizar(UUID id, Gasto gastoAtualizado) {
        return gastoRepository.findById(id)
                .map(gasto -> {
                    gasto.setValor(gastoAtualizado.getValor());
                    return gastoRepository.save(gasto);
                })
                .orElseThrow(() -> new RuntimeException("Gasto não encontrado com id: " + id));
    }

    public void deletar(UUID id) {
        gastoRepository.deleteById(id);
    }

    // Calcular total de todos os gastos (ADMIN - não filtrado)
    public Double calcularTotal() {
        return gastoRepository.findAll().stream()
                .mapToDouble(Gasto::getValor)
                .sum();
    }

    // Calcular total dos gastos de um usuário
    public Double calcularTotalPorUsuario(UUID usuarioId) {
        return gastoRepository.findByUsuario_Id(usuarioId).stream()
                .mapToDouble(Gasto::getValor)
                .sum();
    }

    // Calcular total por tipo (não filtrado)
    public Double calcularTotalPorTipo(String tipo) {
        return gastoRepository.findByCategoria_Tipo(tipo).stream()
                .mapToDouble(Gasto::getValor)
                .sum();
    }

    // Calcular total por tipo e usuário
    public Double calcularTotalPorTipoEUsuario(String tipo, UUID usuarioId) {
        return gastoRepository.findByUsuario_IdAndCategoria_Tipo(usuarioId, tipo).stream()
                .mapToDouble(Gasto::getValor)
                .sum();
    }

    // Buscar gastos do dia
    public List<Gasto> buscarGastosDoDia(UUID usuarioId) {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime fimDia = LocalDate.now().atTime(LocalTime.MAX);
        return gastoRepository.findByUsuarioAndPeriodo(usuarioId, inicioDia, fimDia);
    }

    // Buscar gastos da semana
    public List<Gasto> buscarGastosDaSemana(UUID usuarioId) {
        LocalDateTime inicioSemana = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime fimSemana = LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        return gastoRepository.findByUsuarioAndPeriodo(usuarioId, inicioSemana, fimSemana);
    }

    // Buscar gastos do mês
    public List<Gasto> buscarGastosDoMes(UUID usuarioId) {
        LocalDateTime inicioMes = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime fimMes = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
        return gastoRepository.findByUsuarioAndPeriodo(usuarioId, inicioMes, fimMes);
    }

    // Buscar gastos dos últimos 6 meses
    public List<Gasto> buscarGastosDosUltimos6Meses(UUID usuarioId) {
        LocalDateTime inicio = LocalDate.now().minusMonths(6).atStartOfDay();
        LocalDateTime fim = LocalDate.now().atTime(LocalTime.MAX);
        return gastoRepository.findByUsuarioAndPeriodo(usuarioId, inicio, fim);
    }

    // Buscar gastos do ano
    public List<Gasto> buscarGastosDoAno(UUID usuarioId) {
        LocalDateTime inicioAno = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
        LocalDateTime fimAno = LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX);
        return gastoRepository.findByUsuarioAndPeriodo(usuarioId, inicioAno, fimAno);
    }

    // Calcular total por período
    public Double calcularTotalPorPeriodo(UUID usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        return gastoRepository.findByUsuarioAndPeriodo(usuarioId, inicio, fim).stream()
                .mapToDouble(Gasto::getValor)
                .sum();
    }
}
