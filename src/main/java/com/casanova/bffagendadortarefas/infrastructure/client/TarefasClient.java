package com.casanova.bffagendadortarefas.infrastructure.client;

import com.casanova.bffagendadortarefas.business.dto.In.TarefaDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
import com.casanova.bffagendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Feign Client responsável pela comunicação HTTP declarativa com o microsserviço de Tarefas.
 *
 * - name: Nome lógico do serviço no ecossistema.
 * - contextId: Identificador único obrigatório para evitar colisão de beans quando múltiplos
 *   clientes compartilham nomes ou propriedades semelhantes no Spring.
 * - url: Endereço dinâmico resolvido de forma segura via application.properties (${agendador-tarefas.url}).
 */
@FeignClient(name = "tarefas-service", contextId = "tarefasClient", url = "${agendador-tarefas.url}")
public interface TarefasClient {

    // Cada método exige rigorosamente um verbo HTTP mapeado para que o OpenFeign construa o contrato com sucesso.

    @GetMapping("/tarefas")
    List<TarefaDTOResponse> buscaTarefaPorEmail(@RequestParam("email") String email,
                                                @RequestHeader(value = "Authorization", required=false) String token);

    @PostMapping
    TarefaDTOResponse gravaTarefa(@RequestBody TarefaDTORequest dto,
                                  @RequestHeader(value = "Authorization", required=false) String token);

    @GetMapping("/eventos")
    List<TarefaDTOResponse> buscaTarefasAgendadasPorPeriodo(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataFinal,
            @RequestHeader(value = "Authorization", required=false) String token);

    @GetMapping
    List<TarefaDTOResponse> buscaTarefasAgendadasPorUsuario(@RequestHeader(value = "Authorization", required=false) String token);

    @DeleteMapping
    void deletaTarefaPorId(@RequestParam("id") String id,
                           @RequestHeader(value = "Authorization", required=false) String token);

    @PatchMapping
    TarefaDTOResponse alteraStatusNotificacao(@RequestParam("status") StatusNotificacaoEnum status,
                                              @RequestParam("id") String id,
                                              @RequestHeader(value = "Authorization", required=false) String token);

    @PutMapping
    TarefaDTOResponse atualizaTarefa(@RequestBody TarefaDTOResponse dto,
                                     @RequestParam("id") String id,
                                     @RequestHeader(value = "Authorization", required=false) String token);
}
