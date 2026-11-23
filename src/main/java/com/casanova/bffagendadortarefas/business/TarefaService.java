package com.casanova.bffagendadortarefas.business;

import com.casanova.bffagendadortarefas.business.dto.In.TarefaDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
import com.casanova.bffagendadortarefas.infrastructure.client.TarefasClient;
import com.casanova.bffagendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefasClient tarefasClient;

    public TarefaDTOResponse gravaTarefa(String token, TarefaDTORequest dto) {
        return tarefasClient.gravaTarefa(dto, token);
    }

    public List<TarefaDTOResponse> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal, String token) {
        return tarefasClient.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal, token);
    }

    public List<TarefaDTOResponse> buscaTarefasAgendadasPorUsuario(String token) {
        return tarefasClient.buscaTarefasAgendadasPorUsuario(token);
    }

    public List<TarefaDTOResponse> buscaTarefaPorEmail(String email, String token) {
        return tarefasClient.buscaTarefaPorEmail(email, token);
    }

    public void deletaTarefaPorId(String id, String token) {
        tarefasClient.deletaTarefaPorId(id, token);
    }

    public TarefaDTOResponse alteraStatus(StatusNotificacaoEnum status, String id, String token) {
        return tarefasClient.alteraStatusNotificacao(status, id, token);
    }

    // CORREÇÃO: Atualizado para receber TarefaDTORequest como corpo de entrada
    public TarefaDTOResponse atualizaTarefa(TarefaDTORequest dto, String id, String token) {
        return tarefasClient.atualizaTarefa(dto, id, token);
    }
}
