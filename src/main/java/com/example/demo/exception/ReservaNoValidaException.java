package com.example.demo.exception;

public class ReservaNoValidaException extends RuntimeException {
    public ReservaNoValidaException(String mensaje) {
        super(mensaje);
    }
}
