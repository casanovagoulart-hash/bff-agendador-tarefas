package com.casanova.bffagendadortarefas.business;

import com.casanova.bffagendadortarefas.business.dto.In.LoginRequestDTO;
import com.casanova.bffagendadortarefas.business.dto.In.TarefaDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
import com.casanova.bffagendadortarefas.infrastructure.client.TarefasClient;
import com.casanova.bffagendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de Regra de Negócio para o gerenciamento de Tarefas no BFF.
 * Atua como orquestrador, mandando e recebendo dados do client OpenFeign.
 */
@Service
@RequiredArgsConstructor
public class TarefaService {

    // Cliente OpenFeign responsável pela comunicação HTTP declarativa com o microsserviço Core de Tarefas
    private final TarefasClient tarefasClient;

    /**
     * Encaminha os dados da nova tarefa para serem gravados no microsserviço de destino.
     */
    public TarefaDTOResponse gravaTarefa(String token, TarefaDTORequest dto) {
        return tarefasClient.gravaTarefa(dto, token);
    }

    /**
     * Busca tarefas agendadas em um intervalo de datas específico.
     */
    public List<TarefaDTOResponse> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal, String token) {
        return tarefasClient.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal, token);
    }

    /**
     * Busca todas as tarefas associadas ao usuário logado através do token de autenticação.
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
     * Solicita ao microsserviço a exclusão de uma tarefa com base no seu ID único.
     */
    public void deletaTarefaPorId(String id, String token) {
        tarefasClient.deletaTarefaPorId(id, token);
    }

    /**
     * Altera o status de notificação de uma tarefa específica repassando o ID.
     */
    public TarefaDTOResponse alteraStatus(StatusNotificacaoEnum status, String id, String token) {
        return tarefasClient.alteraStatusNotificacao(status, id, token);
    }

    /**
     * Atualiza os dados cadastrais de uma tarefa existente informando o ID.
     */
    public TarefaDTOResponse atualizaTarefa(TarefaDTOResponse dto, String id, String token) {
        return tarefasClient.atualizaTarefa(dto, id, token);
    }

    /**
     * Realiza o login institucional para obtenção de token de sistema.
     */
    public String loginUsuario(LoginRequestDTO dto) {
        return tarefasClient.loginUsuario(dto);
    }
}
