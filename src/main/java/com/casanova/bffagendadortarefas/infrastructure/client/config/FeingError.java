package com.casanova.bffagendadortarefas.infrastructure.client.config;

import com.casanova.bffagendadortarefas.infrastructure.exceptions.BusinessExceptiom;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.ConflictException;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.IllegalArgumentException;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.UnauthorizedExceptiom;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FeingError implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        String mensagenErro = mensagenErro(response);

        switch (response.status()) {
            case 409:
                return new ConflictException("Erro: " + mensagenErro);
            case 403:
                return new ResourceNotFoundException("Erro: " + mensagenErro);
            case 401:
                return new UnauthorizedExceptiom("Erro: " + mensagenErro);
            case 400:
                return new IllegalArgumentException("Erro: " + mensagenErro);

            default:
                return new BusinessExceptiom("Erro: " + mensagenErro);


        }
    }

    private String mensagenErro(Response response) {
        try {
            if(Objects.isNull(response.body())){
                return "";
            }
            return new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
