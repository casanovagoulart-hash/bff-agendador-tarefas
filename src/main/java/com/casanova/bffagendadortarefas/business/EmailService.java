 package com.casanova.bffagendadortarefas.business;


 import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
 import com.casanova.bffagendadortarefas.infrastructure.client.EmailClient;
 import lombok.RequiredArgsConstructor;
 import org.springframework.stereotype.Service;

 /**
 * Camada de Regra de Negocio para o gerenciamento de Tarefas no BFF.
 * Atua como orquestrador, mandando e recebendo dados do client OpenFeign.
 */

@Service
@RequiredArgsConstructor
public class EmailService {

    // Cliente OpenFeign responsavel pela comunicacao HTTP declarativa com o microsservico Core de Tarefas
    private final EmailClient emailClient;

    public void enviaEmail(TarefaDTOResponse dto) {
        emailClient.enviarEmail(dto);
    }

}

