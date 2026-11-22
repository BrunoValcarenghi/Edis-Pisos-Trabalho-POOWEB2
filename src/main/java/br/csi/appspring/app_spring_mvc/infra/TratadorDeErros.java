package br.csi.appspring.app_spring_mvc.infra;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException erro) {
        Map<String, String> campos = erro.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        item -> item.getField(),
                        item -> item.getDefaultMessage(),
                        (anterior, atual) -> anterior));

        Map<String, Object> resposta = Map.of(
                "status", 400,
                "erro", "Dados inválidos.",
                "campos", campos);

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> tratarConflito(DataIntegrityViolationException erro) {
        Map<String, Object> resposta = Map.of(
                "status", 409,
                "erro", "A operação conflita com dados existentes.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }
}
