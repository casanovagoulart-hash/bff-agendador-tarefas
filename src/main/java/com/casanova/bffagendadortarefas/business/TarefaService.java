package com.casanova.bffagendadortarefas.business;

import com.casanova.bffagendadortarefas.business.dto.In.TarefaDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
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
@Service
@RequiredArgsConstructor
public class TarefaService {

    // Cliente OpenFeign responsavel pela comunicacao HTTP declarativa com o microsservico Core de Tarefas
    private final TarefasClient tarefasClient;

    /**
     * Encaminha os dados da nova tarefa para serem gravados no microsservico de destino.
     */
    public TarefaDTOResponse gravaTarefa(String token, TarefaDTORequest dto) {
        return tarefasClient.gravaTarefa(dto, token);
    }

    /**
     * Busca tarefas agendadas em um intervalo de datas especifico.
     */
    public List<TarefaDTOResponse> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal, String token) {
        return tarefasClient.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal, token);
    }

    /**
     * Busca todas as tarefas associadas ao usuario logado atraves do token de autenticacao.
     */
    public List<TarefaDTOResponse> buscaTarefasAgendadasPorUsuario(String token) {
        return tarefasClient.buscaTarefasAgendadasPorUsuario(token);
    }

    /**
     * Busca tarefas filtrando por e-mail e validando o token JWT.
     */
    public List<TarefaDTOResponse> buscaTarefaPorEmail(String email, String token) {
        return tarefasClient.buscaTarefaPorEmail(email, token);
    }

    /**
     * Solicita ao microsservico a exclusao de uma tarefa com base no seu ID unico.
     */
    public void deletaTarefaPorId(String id, String token) {
        tarefasClient.deletaTarefaPorId(id, token);
    }

    /**
     * Altera o status de notificacao de uma tarefa especifica.
     */
    public TarefaDTOResponse alteraStatus(StatusNotificacaoEnum status, String id, String token) {
        return tarefasClient.alteraStatusNotificacao(status, id, token);
    }

    /**
     * Atualiza os dados cadastrais de uma tarefa existente.
     */
    public TarefaDTOResponse atualizaTarefa(TarefaDTOResponse dto, String id, String token) {
        return tarefasClient.atualizaTarefa(dto, id, token);
    }
}
