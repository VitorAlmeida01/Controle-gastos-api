package gastos.app.controlegastos.controller;

import gastos.app.controlegastos.dto.Analytics.AvgCategoriaDto;
import gastos.app.controlegastos.dto.Analytics.SeasonalityDto;
import gastos.app.controlegastos.dto.Gasto.GastoRequestDto;
import gastos.app.controlegastos.dto.Gasto.GastoResponseDto;
import gastos.app.controlegastos.entity.Categoria;
import gastos.app.controlegastos.entity.Gasto;
import gastos.app.controlegastos.entity.Usuario;
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
    public ResponseEntity<?> listarTodos() {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Buscar apenas gastos do usuário logado
            List<Gasto> gastos = gastoService.listarPorUsuario(usuario.getId());
            List<GastoResponseDto> response = gastos.stream()
                    .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo(), g.getDtCriacao(), g.getDescricao()))
                    .toList();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Buscar gasto e validar se pertence ao usuário
            return gastoService.buscarPorIdEUsuario(id, usuario.getId())
                    .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo(), g.getDtCriacao(), g.getDescricao()))
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> buscarPorTipo(@PathVariable String tipo) {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Buscar gastos do usuário filtrados por tipo
            List<Gasto> gastos = gastoService.buscarPorTipoEUsuario(tipo, usuario.getId());
            List<GastoResponseDto> response = gastos.stream()
                    .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo(), g.getDtCriacao(), g.getDescricao()))
                    .toList();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    // Novos endpoints para buscar por período
    @GetMapping("/periodo/dia")
    public ResponseEntity<?> buscarGastosDoDia() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            List<Gasto> gastos = gastoService.buscarGastosDoDia(usuario.getId());
            List<GastoResponseDto> response = gastos.stream()
                    .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo(), g.getDtCriacao(), g.getDescricao()))
                    .toList();

            Double total = gastos.stream().mapToDouble(Gasto::getValor).sum();

            return ResponseEntity.ok(java.util.Map.of(
                    "gastos", response,
                    "total", total,
                    "quantidade", gastos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/periodo/semana")
    public ResponseEntity<?> buscarGastosDaSemana() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            List<Gasto> gastos = gastoService.buscarGastosDaSemana(usuario.getId());
            List<GastoResponseDto> response = gastos.stream()
                    .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo(), g.getDtCriacao(), g.getDescricao()))
                    .toList();

            Double total = gastos.stream().mapToDouble(Gasto::getValor).sum();

            return ResponseEntity.ok(java.util.Map.of(
                    "gastos", response,
                    "total", total,
                    "quantidade", gastos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/periodo/mes")
    public ResponseEntity<?> buscarGastosDoMes() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            List<Gasto> gastos = gastoService.buscarGastosDoMes(usuario.getId());
            List<GastoResponseDto> response = gastos.stream()
                    .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo(), g.getDtCriacao(), g.getDescricao()))
                    .toList();

            Double total = gastos.stream().mapToDouble(Gasto::getValor).sum();

            return ResponseEntity.ok(java.util.Map.of(
                    "gastos", response,
                    "total", total,
                    "quantidade", gastos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/periodo/6meses")
    public ResponseEntity<?> buscarGastosDosUltimos6Meses() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            List<Gasto> gastos = gastoService.buscarGastosDosUltimos6Meses(usuario.getId());
            List<GastoResponseDto> response = gastos.stream()
                    .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo(), g.getDtCriacao(), g.getDescricao()))
                    .toList();

            Double total = gastos.stream().mapToDouble(Gasto::getValor).sum();

            return ResponseEntity.ok(java.util.Map.of(
                    "gastos", response,
                    "total", total,
                    "quantidade", gastos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/periodo/ano")
    public ResponseEntity<?> buscarGastosDoAno() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            List<Gasto> gastos = gastoService.buscarGastosDoAno(usuario.getId());
            List<GastoResponseDto> response = gastos.stream()
                    .map(g -> new GastoResponseDto(g.getId(), g.getValor(), g.getCategoria().getTipo(), g.getDtCriacao(), g.getDescricao()))
                    .toList();

            Double total = gastos.stream().mapToDouble(Gasto::getValor).sum();

            return ResponseEntity.ok(java.util.Map.of(
                    "gastos", response,
                    "total", total,
                    "quantidade", gastos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody GastoRequestDto gastoDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Categoria categoria = categoriaService.buscarCategoriaPorIdEUsuario(
                    gastoDto.getCategoriaId(), usuario);

            Gasto gasto = new Gasto();
            gasto.setValor(gastoDto.getValor());
            gasto.setCategoria(categoria);
            gasto.setUsuario(usuario);
            gasto.setDescricao(gastoDto.getDescricao());

            Gasto novoGasto = gastoService.salvar(gasto);

            GastoResponseDto response = new GastoResponseDto(
                novoGasto.getId(),
                novoGasto.getValor(),
                novoGasto.getCategoria().getTipo(),
                novoGasto.getDtCriacao(),
                novoGasto.getDescricao()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", "Erro ao criar gasto: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable UUID id, @RequestBody GastoRequestDto gastoDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Gasto gastoExistente = gastoService.buscarPorIdEUsuario(id, usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Gasto não encontrado ou não pertence ao usuário"));

            Categoria categoria = categoriaService.buscarCategoriaPorIdEUsuario(
                    gastoDto.getCategoriaId(), usuario);

            gastoExistente.setValor(gastoDto.getValor());
            gastoExistente.setCategoria(categoria);
            gastoExistente.setDescricao(gastoDto.getDescricao());

            Gasto gastoAtualizado = gastoService.salvar(gastoExistente);

            GastoResponseDto response = new GastoResponseDto(
                gastoAtualizado.getId(),
                gastoAtualizado.getValor(),
                gastoAtualizado.getCategoria().getTipo(),
                gastoAtualizado.getDtCriacao(),
                gastoAtualizado.getDescricao()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", "Erro ao atualizar gasto: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable UUID id) {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Validar se o gasto pertence ao usuário antes de deletar
            Gasto gasto = gastoService.buscarPorIdEUsuario(id, usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Gasto não encontrado ou não pertence ao usuário"));

            gastoService.deletar(id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", "Erro ao deletar gasto: " + e.getMessage()));
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> calcularTotal() {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Calcular total apenas dos gastos do usuário logado
            Double total = gastoService.calcularTotalPorUsuario(usuario.getId());
            return ResponseEntity.ok(total);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/total/tipo/{tipo}")
    public ResponseEntity<?> calcularTotalPorTipo(@PathVariable String tipo) {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Calcular total por tipo apenas dos gastos do usuário logado
            Double total = gastoService.calcularTotalPorTipoEUsuario(tipo, usuario.getId());
            return ResponseEntity.ok(total);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/analytics/media-categoria")
    public ResponseEntity<?> mediaPorCategoria(@RequestParam(name = "scope", defaultValue = "usuario") String scope) {
        try {
            UUID usuarioId = null;
            if ("usuario".equalsIgnoreCase(scope)) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();
                Usuario usuario = usuarioService.buscarPorEmail(email)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                usuarioId = usuario.getId();
            }

            List<AvgCategoriaDto> response = gastoService.mediaPorCategoria(usuarioId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/analytics/ticket-medio")
    public ResponseEntity<?> ticketMedio(@RequestParam(name = "scope", defaultValue = "usuario") String scope) {
        try {
            UUID usuarioId = null;
            if ("usuario".equalsIgnoreCase(scope)) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();
                Usuario usuario = usuarioService.buscarPorEmail(email)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                usuarioId = usuario.getId();
            }

            Double response = gastoService.ticketMedio(usuarioId);
            return ResponseEntity.ok(java.util.Map.of("ticketMedio", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/analytics/sazonalidade")
    public ResponseEntity<?> sazonalidade(
            @RequestParam(name = "granularidade", defaultValue = "mes") String granularidade,
            @RequestParam(name = "scope", defaultValue = "usuario") String scope) {
        try {
            UUID usuarioId = null;
            if ("usuario".equalsIgnoreCase(scope)) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();
                Usuario usuario = usuarioService.buscarPorEmail(email)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                usuarioId = usuario.getId();
            }

            if ("dia-semana".equalsIgnoreCase(granularidade)) {
                List<SeasonalityDto> response = gastoService.sazonalidadePorDiaSemana(usuarioId);
                return ResponseEntity.ok(response);
            }

            List<SeasonalityDto> response = gastoService.sazonalidadePorMes(usuarioId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("erro", e.getMessage()));
        }
    }
}
