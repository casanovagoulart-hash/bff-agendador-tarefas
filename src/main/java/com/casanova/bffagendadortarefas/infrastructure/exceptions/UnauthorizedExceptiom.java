package com.casanova.bffagendadortarefas.infrastructure.exceptions;

public class UnauthorizedExceptiom extends RuntimeException {

    public UnauthorizedExceptiom(String message) {
        super(message);
    }

    public UnauthorizedExceptiom(String message, Throwable cause) {
        super(message, cause);
    }


}
