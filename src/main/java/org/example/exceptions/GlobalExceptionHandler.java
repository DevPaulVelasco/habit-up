package org.example.exceptions;

import org.example.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Este componente atrapa todos los errores de la app y los formatea
 * para que el Frontend de Alex sepa qué mostrar.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. ATRAPA ERRORES DE VALIDACIÓN (Vista 12/13 - Registro/Login)
    // Cuando Alex deja campos vacíos o mal escritos.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación en los datos")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 2. ERRORES DE AUTORIZACIÓN (Login fallido o acceso denegado) 401
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // 3. ERRORES DE RECURSO NO ENCONTRADO (Ids incorrectos) 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value()) // HTTP 404 Not Found
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 4. ATRAPA ERRORES DE LÓGICA (RuntimeExceptions) - 400
    // Usado para reglas de negocio como "El correo ya existe"
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage()) // Mantener mensaje general
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 5. ERROR GENÉRICO (Cualquier cosa inesperada) 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Ocurrió un error inesperado en el servidor: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}