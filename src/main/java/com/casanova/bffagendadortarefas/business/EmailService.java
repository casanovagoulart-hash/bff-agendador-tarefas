/* package com.casanova.bffagendadortarefas.business;


import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTO;
import com.casanova.bffagendadortarefas.infrastructure.client.EmailClient;
import com.casanova.bffagendadortarefas.infrastructure.client.TarefasClient;
import com.casanova.bffagendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de Regra de Negocio para o gerenciamento de Tarefas no BFF.
 * Atua como orquestrador, mandando e recebendo dados do client OpenFeign.
 */
/*
@Service
@RequiredArgsConstructor
public class EmailService {

    // Cliente OpenFeign responsavel pela comunicacao HTTP declarativa com o microsservico Core de Tarefas
    private final EmailClient emailClient;

    public void enviaEmail(TarefaDTO dto) {
        emailClient.enviarEmail(dto);
    }

}

*/