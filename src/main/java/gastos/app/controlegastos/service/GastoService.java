package gastos.app.controlegastos.service;

import gastos.app.controlegastos.entity.Gasto;
import gastos.app.controlegastos.enums.CategoriaEnum;
import gastos.app.controlegastos.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;

    public List<Gasto> listarTodos() {
        return gastoRepository.findAll();
    }

    public Optional<Gasto> buscarPorId(UUID id) {
        return gastoRepository.findById(id);
    }

    public List<Gasto> buscarPorTipo(CategoriaEnum tipo) {
        return gastoRepository.findByCategoria_Tipo(tipo);
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

    public Double calcularTotal() {
        return gastoRepository.findAll().stream()
                .mapToDouble(Gasto::getValor)
                .sum();
    }

    public Double calcularTotalPorTipo(CategoriaEnum tipo) {
        return gastoRepository.findByCategoria_Tipo(tipo).stream()
                .mapToDouble(Gasto::getValor)
                .sum();
    }
}
