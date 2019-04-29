package com.revolut.hiring.exceptions;

public class InsufficientFundsException extends RuntimeException {

    /** */
    private static final long serialVersionUID = 1L;
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

    /**
     * @return The message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @return The cause
     */
    @Override
    public Throwable getCause() {
        return cause;
    }

}
