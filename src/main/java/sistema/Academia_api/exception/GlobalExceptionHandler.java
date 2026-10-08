package sistema.Academia_api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j 
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> recursoNaoEncontrado(
            RecursoNaoEncontradoException e) {

        log.warn("Recurso não encontrado: {}", e.getMessage());

        return resposta(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(EmailJaCadastrado.class)
    public ResponseEntity<Map<String, Object>> emailJaCadastrado(
            EmailJaCadastrado e) {

        return resposta(
                HttpStatus.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(CpfJaCadastrado.class)
    public ResponseEntity<Map<String, Object>> cpfJaCadastrado(
            CpfJaCadastrado e) {

        return resposta(
                HttpStatus.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> regraNegocio(
            RegraNegocioException e) {

        return resposta(
                HttpStatus.CONFLICT,
                e.getMessage()
        );
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Map<String, Object>> credenciaisInvalidas(
            CredenciaisInvalidasException e) {

        return resposta(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacao(
            MethodArgumentNotValidException e) {
        log.warn("Requisição rejeitada por dados inválidos");

        Map<String, Object> erros = new HashMap<>();

        erros.put("status", 400);
        erros.put("erro", "Dados inválidos");
        erros.put("data", LocalDateTime.now());

        Map<String, String> campos = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(erro ->
                        campos.put(
                                erro.getField(),
                                erro.getDefaultMessage()
                        )
                );

        erros.put("campos", campos);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erros);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> erroInterno(
            Exception e) {

                log.error("Erro interno não tratado", e);

        return resposta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor"
        );
    }

    private ResponseEntity<Map<String, Object>> resposta(
            HttpStatus status,
            String mensagem) {

        Map<String, Object> body = new HashMap<>();

        body.put("status", status.value());
        body.put("erro", mensagem);
        body.put("data", LocalDateTime.now());

        return ResponseEntity
                .status(status)
                .body(body);
    }
}