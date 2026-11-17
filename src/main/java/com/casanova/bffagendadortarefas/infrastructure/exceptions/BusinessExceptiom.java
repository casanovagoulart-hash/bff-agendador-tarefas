package com.casanova.bffagendadortarefas.infrastructure.exceptions;

public class BusinessExceptiom extends RuntimeException {

    public BusinessExceptiom(String message) {
        super(message);
    }

    public BusinessExceptiom(String message, Throwable cause) {
        super(message, cause);
    }


}
