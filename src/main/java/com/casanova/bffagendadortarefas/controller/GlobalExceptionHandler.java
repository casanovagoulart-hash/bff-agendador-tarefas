package com.casanova.bffagendadortarefas.controller;

import com.casanova.bffagendadortarefas.infrastructure.exceptions.BusinessExceptiom;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.ConflictException;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.IllegalArgumentException;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.UnauthorizedExceptiom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// CORREÇÃO: Removido @EnableGlobalAuthentication e seu import.
// ARGUMENTO DIDÁTICO: Essa anotação pertence ao núcleo de configuração do Spring Security
// (e foi descontinuada nas versões atuais do framework). Mantê-la em um @ControllerAdvice
// gera conflito de inicialização de beans no Spring Boot 4.x.
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException ex) {
        return new ResponseEntity<>("Conflict: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedExceptiom.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedExceptiom ex) {
        return new ResponseEntity<>("Unauthorized: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // CORREÇÃO: Adicionado tratamento para BusinessExceptiom (lançada pelo FeingError
    // quando os microsserviços internos retornam falhas genéricas / 500).
    @ExceptionHandler(BusinessExceptiom.class)
    public ResponseEntity<String> handleBusinessException(BusinessExceptiom ex) {
        return new ResponseEntity<>("Business Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
