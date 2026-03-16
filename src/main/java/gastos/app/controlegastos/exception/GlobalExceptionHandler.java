package gastos.app.controlegastos.exception;

import gastos.app.controlegastos.enums.CategoriaEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");

        String message = ex.getMessage();

        // Verificar se é um erro relacionado ao enum CategoriaEnum
        if (message != null && message.contains("CategoriaEnum")) {
            errorResponse.put("message", "Tipo de categoria inválido. Verifique os valores permitidos.");
            errorResponse.put("valoresPermitidos", Arrays.stream(CategoriaEnum.values())
                .map(CategoriaEnum::getDescricao)
                .collect(Collectors.toList()));
        } else if (message != null && message.contains("Tipo de categoria inválido")) {
            errorResponse.put("message", ex.getCause() != null ? ex.getCause().getMessage() : message);
            errorResponse.put("valoresPermitidos", Arrays.stream(CategoriaEnum.values())
                .map(CategoriaEnum::getDescricao)
                .collect(Collectors.toList()));
        } else {
            errorResponse.put("message", "Requisição inválida. Verifique o formato do JSON enviado.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage());

        // Se for erro de categoria inválida, adicionar valores permitidos
        if (ex.getMessage() != null && ex.getMessage().contains("Tipo de categoria inválido")) {
            errorResponse.put("valoresPermitidos", Arrays.stream(CategoriaEnum.values())
                .map(CategoriaEnum::getDescricao)
                .collect(Collectors.toList()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

