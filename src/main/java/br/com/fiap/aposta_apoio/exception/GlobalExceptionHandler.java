package br.com.fiap.aposta_apoio.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import br.com.fiap.aposta_apoio.model.Especialidade;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "Operação violou integridade de dados (FK ou unique).", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String,String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> {
                    Map<String,String> m = new HashMap<>();
                    m.put("campo", err.getField());
                    m.put("mensagem", err.getDefaultMessage());
                    return m;
                }).collect(Collectors.toList());
        return build(HttpStatus.BAD_REQUEST, "Erro de validação", fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleNotReadable(HttpMessageNotReadableException ex) {
        String mensagem = "Payload inválido ou formato incorreto.";
        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof InvalidFormatException ife) {
            if (!ife.getPath().isEmpty()) {
                String campo = ife.getPath().get(0).getFieldName();
                if (ife.getTargetType() != null && ife.getTargetType().equals(Especialidade.class)) {
                    mensagem = "Valor inválido para '" + campo + "'. Valores permitidos: " + String.join(", ",
                            java.util.Arrays.stream(Especialidade.values()).map(Enum::name).toList()) + ".";
                } else if (ife.getTargetType() != null && (ife.getTargetType().getSimpleName().equals("LocalDate") || ife.getTargetType().getSimpleName().equals("LocalDateTime"))) {
                    mensagem = "Formato de data/hora inválido para '" + campo + "'. Use ISO-8601 (ex: 2025-09-20T14:30:00).";
                } else {
                    mensagem = "Valor inválido para campo '" + campo + "'.";
                }
            }
        }
        return build(HttpStatus.BAD_REQUEST, mensagem, null);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<?> handleRestClient(RestClientException ex) {
        return build(HttpStatus.BAD_GATEWAY, "Falha ao consumir serviço externo", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", null);
    }

    private ResponseEntity<Map<String,Object>> build(HttpStatus status, String message, List<Map<String,String>> fieldErrors) {
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("erro", status.getReasonPhrase());
        body.put("mensagem", message);
        if(fieldErrors != null && !fieldErrors.isEmpty()) body.put("erros", fieldErrors);
        return ResponseEntity.status(status).body(body);
    }
}
