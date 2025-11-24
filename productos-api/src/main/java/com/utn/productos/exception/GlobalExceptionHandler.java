package com.utn.productos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Producto no encontrado
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductoNotFound(
            ProductoNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 400 - Validación de DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String mensaje = ex.getBindingResult().getFieldError().getDefaultMessage();

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(mensaje)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 500 - Errores generales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralErrors(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error interno del servidor")
                .path(request.getRequestURI())
                .build();

        ex.printStackTrace(); // útil para debugging

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
