package gastos.app.controlegastos.service;

import gastos.app.controlegastos.dto.Categoria.CategoriaDto;
import gastos.app.controlegastos.entity.Categoria;
import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria buscarOuCriarCategoria(Usuario usuario, String tipo) {
        return categoriaRepository.findByUsuarioAndTipo(usuario, tipo)
                .orElseGet(() -> {
                    Categoria novaCategoria = new Categoria();
                    novaCategoria.setTipo(tipo);
                    novaCategoria.setUsuario(usuario);
                    novaCategoria.setDtCriacao(LocalDate.now());
                    return categoriaRepository.save(novaCategoria);
                });
    }

    public Categoria cadastrarCategoria(Usuario usuario, CategoriaDto categoriaDto) {
        String tipo = categoriaDto.getTipo();
        if (categoriaRepository.findByUsuarioAndTipo(usuario, tipo).isPresent()) {
            throw new RuntimeException("Categoria com esse nome já existe para este usuário");
        }
        Categoria categoria = new Categoria();
        categoria.setTipo(tipo);
        categoria.setUsuario(usuario);
        categoria.setDtCriacao(LocalDate.now());
        return categoriaRepository.save(categoria);
    }

    public Categoria buscarCategoriaPorIdEUsuario(UUID categoriaId, Usuario usuario) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Validar se a categoria pertence ao usuário
        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Categoria não pertence ao usuário");
        }

        return categoria;
    }

    public Optional<Categoria> buscarPorId(UUID id) {
        return categoriaRepository.findById(id);
    }

    public Categoria editarCategoria(Categoria categoria, CategoriaDto dto) {
        categoria.setTipo(dto.getTipo());
        return categoriaRepository.save(categoria);
    }

    public void excluirCategoria(UUID id) {
        categoriaRepository.deleteById(id);
    }
}
