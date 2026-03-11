package gastos.app.controlegastos.controller;

import gastos.app.controlegastos.dto.Gasto.GastoRequestDto;
import gastos.app.controlegastos.dto.Gasto.GastoResponseDto;
import gastos.app.controlegastos.entity.Categoria;
import gastos.app.controlegastos.entity.Gasto;
import gastos.app.controlegastos.entity.Usuario;
import gastos.app.controlegastos.enums.CategoriaEnum;
import gastos.app.controlegastos.service.CategoriaService;
import gastos.app.controlegastos.service.GastoService;
import gastos.app.controlegastos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<GastoResponseDto>> listarTodos() {
        List<Gasto> gastos = gastoService.listarTodos();
        List<GastoResponseDto> response = gastos.stream()
                .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoResponseDto> buscarPorId(@PathVariable UUID id) {
        return gastoService.buscarPorId(id)
                .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<GastoResponseDto>> buscarPorTipo(@PathVariable CategoriaEnum tipo) {
        List<Gasto> gastos = gastoService.buscarPorTipo(tipo);
        List<GastoResponseDto> response = gastos.stream()
                .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<GastoResponseDto> criar(@RequestBody GastoRequestDto gastoDto) {
        // Obter usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Buscar ou criar categoria para este usuário
        Categoria categoria = categoriaService.buscarOuCriarCategoria(usuario, gastoDto.getTipo());

        // Criar gasto
        Gasto gasto = new Gasto();
        gasto.setValor(gastoDto.getValor());
        gasto.setCategoria(categoria);

        Gasto novoGasto = gastoService.salvar(gasto);

        // Criar resposta DTO
        GastoResponseDto response = new GastoResponseDto(
            novoGasto.getId(),
            novoGasto.getValor(),
            novoGasto.getCategoria().getTipo()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastoResponseDto> atualizar(@PathVariable UUID id, @RequestBody GastoRequestDto gastoDto) {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Buscar gasto existente
            Gasto gastoExistente = gastoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Gasto não encontrado"));

            // Buscar ou criar categoria
            Categoria categoria = categoriaService.buscarOuCriarCategoria(usuario, gastoDto.getTipo());

            // Atualizar gasto
            gastoExistente.setValor(gastoDto.getValor());
            gastoExistente.setCategoria(categoria);

            Gasto gastoAtualizado = gastoService.salvar(gastoExistente);

            // Criar resposta DTO
            GastoResponseDto response = new GastoResponseDto(
                gastoAtualizado.getId(),
                gastoAtualizado.getValor(),
                gastoAtualizado.getCategoria().getTipo()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        gastoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Double> calcularTotal() {
        Double total = gastoService.calcularTotal();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/tipo/{tipo}")
    public ResponseEntity<Double> calcularTotalPorTipo(@PathVariable CategoriaEnum tipo) {
        Double total = gastoService.calcularTotalPorTipo(tipo);
        return ResponseEntity.ok(total);
    }
}
