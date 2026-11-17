package com.casanova.bffagendadortarefas.infrastructure.client.config;

import com.casanova.bffagendadortarefas.infrastructure.exceptions.BusinessExceptiom;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.ConflictException;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.casanova.bffagendadortarefas.infrastructure.exceptions.UnauthorizedExceptiom;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeingError implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()) {
            case 409:
                return new ConflictException("Erro atributo já existente.");
            case 403:
                return new ResourceNotFoundException("Erro atributo não encontrado.");
            case 401:
                return new UnauthorizedExceptiom("Erro usuário não autorizado.");

            default:
                return new BusinessExceptiom("Erro de servidor.");


        }
    }


}
