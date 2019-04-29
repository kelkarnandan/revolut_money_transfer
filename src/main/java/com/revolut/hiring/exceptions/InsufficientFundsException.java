package com.revolut.hiring.exceptions;

public class InsufficientFundsException extends RuntimeException {

    private String message;
    private Throwable cause;

    public InsufficientFundsException() {
        super();
    }
    public InsufficientFundsException(String message) {
        super(message);
    }
    public InsufficientFundsException(Throwable cause) {
        super(cause);
    }
    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
