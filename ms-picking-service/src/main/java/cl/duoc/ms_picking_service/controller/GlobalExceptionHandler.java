package cl.duoc.ms_picking_service.controller;

import cl.duoc.ms_picking_service.service.exception.OrdenPickingNoEncontradaException;
import cl.duoc.ms_picking_service.service.exception.OrdenPickingYaExisteException;
import cl.duoc.ms_picking_service.service.exception.OrdenYaDespachadaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrdenPickingNoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleNoEncontrada(OrdenPickingNoEncontradaException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(OrdenPickingYaExisteException.class)
    public ResponseEntity<Map<String, Object>> handleYaExiste(OrdenPickingYaExisteException ex) {
        return error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(OrdenYaDespachadaException.class)
    public ResponseEntity<Map<String, Object>> handleYaDespachada(OrdenYaDespachadaException ex) {
        return error(HttpStatus.CONFLICT, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "mensaje", mensaje
        ));
    }
}