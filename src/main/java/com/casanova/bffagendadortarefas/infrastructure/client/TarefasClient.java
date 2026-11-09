package com.casanova.bffagendadortarefas.infrastructure.client;

import com.casanova.bffagendadortarefas.business.dto.In.LoginRequestDTO;
import com.casanova.bffagendadortarefas.business.dto.In.TarefaDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
import com.casanova.bffagendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface OpenFeign responsável pela comunicação HTTP declarativa
 * entre o BFF e o microsserviço Core de Tarefas.
 */
@FeignClient(name = "agendador-tarefas", url = "${agendador-tarefas.url}")
public interface TarefasClient {

    /**
     * Envia os dados de uma nova tarefa para serem gravados no microsserviço Core.
     */
    @PostMapping
    TarefaDTOResponse gravaTarefa(
            @RequestBody TarefaDTORequest dto,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    /**
     * Busca tarefas agendadas dentro de um intervalo específico de datas e horas.
     */
    @GetMapping("/periodo")
    List<TarefaDTOResponse> buscaTarefasAgendadasPorPeriodo(
            @RequestParam("dataInicial") LocalDateTime dataInicial,
            @RequestParam("dataFinal") LocalDateTime dataFinal,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    /**
     * Retorna todas as tarefas associadas ao usuário dono do token JWT enviado.
     */
    @GetMapping("/usuario")
    List<TarefaDTOResponse> buscaTarefasAgendadasPorUsuario(
            @RequestHeader(value = "Authorization", required = false) String token
    );

    /**
     * Busca tarefas filtrando diretamente pelo e-mail do usuário.
     */
    @GetMapping("/email")
    List<TarefaDTOResponse> buscaTarefaPorEmail(
            @RequestParam("email") String email,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    /**
     * Remove uma tarefa do sistema com base no seu ID único.
     */
    @DeleteMapping("/{id}")
    void deletaTarefaPorId(
            @PathVariable("id") String id,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    /**
     * Altera o status de notificação de uma tarefa específica.
     */
    @PatchMapping("/{id}")
    TarefaDTOResponse alteraStatusNotificacao(
            @RequestParam("status") StatusNotificacaoEnum status,
            @PathVariable("id") String id,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    /**
     * Atualiza os dados cadastrais de uma tarefa já existente.
     */
    @PutMapping("/{id}")
    TarefaDTOResponse atualizaTarefa(
            @RequestBody TarefaDTOResponse dto,
            @PathVariable("id") String id,
            @RequestHeader(value = "Authorization", required = false) String token
    );

    /**
     * Realiza a autenticação interna do BFF junto ao microsserviço Core
     * para obtenção de credenciais/tokens de sistema (utilizado no agendador Cron).
     */
    @PostMapping("/login")
    String loginUsuario(@RequestBody LoginRequestDTO dto);

}
