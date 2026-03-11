package gastos.app.controlegastos.service;

import gastos.app.controlegastos.dto.Categoria.CategoriaDto;
import gastos.app.controlegastos.entity.Categoria;
import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.enums.CategoriaEnum;
import gastos.app.controlegastos.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;


    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria salvar(CategoriaDto categoriaDto){
        Categoria categoria = new Categoria();
        return categoria;
    }

    public Categoria buscarOuCriarCategoria(Usuario usuario, CategoriaEnum tipo) {
        // Buscar categoria existente para este usuário e tipo
        return categoriaRepository.findByUsuarioAndTipo(usuario, tipo)
                .orElseGet(() -> {
                    // Criar nova categoria se não existir
                    Categoria novaCategoria = new Categoria();
                    novaCategoria.setTipo(tipo);
                    novaCategoria.setUsuario(usuario);
                    novaCategoria.setDtCriacao(LocalDate.now());
                    return categoriaRepository.save(novaCategoria);
                });
    }
}
