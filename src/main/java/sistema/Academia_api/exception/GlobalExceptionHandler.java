package sistema.Academia_api.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AlunoException.class)

    public ResponseEntity<Map<String, String>> tratarAlunoNaoEncontrado(AlunoException e) {

        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler (CpfJaCadastrado.class)
    public ResponseEntity<Map<String, String>> cpfJaCadastrado(CpfJaCadastrado e) {
        return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler (EmailJaCadastrado.class)
    public ResponseEntity<Map<String, String>> emailJaCadastrado(EmailJaCadastrado e) {
        return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(Map.of("error", e.getMessage()));
    }
}
