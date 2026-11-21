package com.casanova.bffagendadortarefas.infrastructure.client;

import com.casanova.bffagendadortarefas.business.dto.In.TarefaDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
import com.casanova.bffagendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// CORREÇÃO: Adicionado path = "/tarefas" para alinhar com @RequestMapping("/tarefas") do Core
@FeignClient(name = "agendador-tarefas", url = "${agendador-tarefas.url}", path = "/tarefas")
public interface TarefasClient {

    @PostMapping
    TarefaDTOResponse gravaTarefa(
            @RequestBody TarefaDTORequest dto,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    // CORREÇÃO: Rota alinhada para "/eventos" com @DateTimeFormat ISO
    @GetMapping("/eventos")
    List<TarefaDTOResponse> buscaTarefasAgendadasPorPeriodo(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    // CORREÇÃO: Rota raiz de /tarefas (retorna tarefas do usuário autenticado via token)
    @GetMapping
    List<TarefaDTOResponse> buscaTarefasAgendadasPorUsuario(
            @RequestHeader(value = "Authorization", required = false) String token
    );

    @GetMapping("/email")
    List<TarefaDTOResponse> buscaTarefaPorEmail(
            @RequestParam("email") String email,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    @DeleteMapping("/{id}")
    void deletaTarefaPorId(
            @PathVariable("id") String id,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    @PatchMapping("/{id}")
    TarefaDTOResponse alteraStatusNotificacao(
            @RequestParam("status") StatusNotificacaoEnum status,
            @PathVariable("id") String id,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    @PutMapping("/{id}")
    TarefaDTOResponse atualizaTarefa(
            @RequestBody TarefaDTORequest dto,
            @PathVariable("id") String id,
            @RequestHeader(value = "Authorization", required = false) String token
    );
}
