package com.utn.productos.exception;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException() {
        super("El stock no puede ser menor que cero");
    }
}
