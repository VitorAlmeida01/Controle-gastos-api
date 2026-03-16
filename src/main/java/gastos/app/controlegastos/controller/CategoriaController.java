package gastos.app.controlegastos.controller;

import gastos.app.controlegastos.dto.Categoria.CategoriaDto;
import gastos.app.controlegastos.entity.Categoria;
import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.service.CategoriaService;
import gastos.app.controlegastos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> cadastrarCategoria(@RequestBody CategoriaDto categoriaDto) {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("erro", "Usuário não autenticado"));
            }

            String email = authentication.getName();

            // Buscar usuário no banco
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Cadastrar categoria para o usuário
            Categoria categoriaSalva = categoriaService.cadastrarCategoria(usuario, categoriaDto);

            // Preparar resposta (categoria sem dados do usuário, pois já está implícito que é do usuário logado)
            Map<String, Object> response = new HashMap<>();
            response.put("id", categoriaSalva.getId());
            response.put("tipo", categoriaSalva.getTipo());
            response.put("dtCriacao", categoriaSalva.getDtCriacao());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao cadastrar categoria: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarCategorias() {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("erro", "Usuário não autenticado"));
            }

            String email = authentication.getName();

            // Buscar usuário no banco
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Buscar categorias do usuário
            List<Categoria> categorias = usuario.getCategorias();

            // Preparar resposta (lista de categorias sem dados do usuário)
            List<Map<String, Object>> response = categorias.stream()
                    .map(cat -> {
                        Map<String, Object> categoriaMap = new HashMap<>();
                        categoriaMap.put("id", cat.getId());
                        categoriaMap.put("tipo", cat.getTipo());
                        categoriaMap.put("dtCriacao", cat.getDtCriacao());
                        return categoriaMap;
                    })
                    .toList();

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao listar categorias: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarCategoria(@PathVariable("id") UUID categoriaId, @RequestBody CategoriaDto categoriaDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("erro", "Usuário não autenticado"));
            }
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Categoria categoria = categoriaService.buscarPorId(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

            if (!categoria.getUsuario().getId().equals(usuario.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("erro", "Categoria não pertence ao usuário logado"));
            }

            Categoria categoriaAtualizada = categoriaService.editarCategoria(categoria, categoriaDto);
            Map<String, Object> response = new HashMap<>();
            response.put("id", categoriaAtualizada.getId());
            response.put("tipo", categoriaAtualizada.getTipo());
            response.put("dtCriacao", categoriaAtualizada.getDtCriacao());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao editar categoria: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCategoria(@PathVariable("id") UUID categoriaId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("erro", "Usuário não autenticado"));
            }
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Categoria categoria = categoriaService.buscarPorId(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

            if (!categoria.getUsuario().getId().equals(usuario.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("erro", "Categoria não pertence ao usuário logado"));
            }

            categoriaService.excluirCategoria(categoriaId);
            return ResponseEntity.ok(Map.of("mensagem", "Categoria excluída com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao excluir categoria: " + e.getMessage()));
        }
    }

}
