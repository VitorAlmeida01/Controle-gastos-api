package gastos.app.controlegastos.controller;

import gastos.app.controlegastos.dto.Usuario.UsuarioRequestDto;
import gastos.app.controlegastos.dto.Usuario.UsuarioResponseDto;
import gastos.app.controlegastos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDto> register(@RequestBody UsuarioRequestDto request){
        UsuarioResponseDto saved = usuarioService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listar(){
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable UUID id, @RequestBody UsuarioRequestDto dto){
        UsuarioResponseDto atualizado = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletar(@PathVariable UUID id){
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
