package com.casanova.bffagendadortarefas.controller;

import com.casanova.bffagendadortarefas.business.TarefaService;
import com.casanova.bffagendadortarefas.business.dto.TarefaDTO;
import com.casanova.bffagendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.casanova.bffagendadortarefas.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller responsável por expor os endpoints REST do BFF (Backend for Frontend)
 * focados no gerenciamento e agendamento de tarefas dos usuários.
 */
@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Gerenciamento e cadastro de tarefas de usuários")
@SecurityRequirement(name = SecurityConfig.SEGURITY_SCHEME)
public class TarefaController {

    private final TarefaService tarefaService;

    /**
     * Recebe a requisição para persistir uma nova tarefa, validando o token JWT no header.
     */
    @PostMapping
    @Operation(summary = "Salvar Tarefa", description = "Cria uma nova tarefa para o usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Tarefa salva com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<TarefaDTO> gravaTarefa(@RequestBody TarefaDTO dto,
                                                 @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.gravaTarefa(token, dto));
    }

    /**
     * Busca tarefas agendadas filtrando por um intervalo de datas (período).
     */
    @GetMapping("/eventos")
    @Operation(summary = "Busca tarefas por período", description = "Busca tarefas cadastradas em um intervalo de datas")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<TarefaDTO>> buscaTarefasAgendadasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal, token));
    }

    /**
     * Recupera a lista de tarefas associadas ao usuário dono do token JWT enviado.
     */
    @GetMapping
    @Operation(summary = "Busca tarefas do usuário", description = "Busca todas as tarefas agendadas do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<TarefaDTO>> buscaTarefasAgendadasPorUsuario(@RequestHeader(name = "Authorization", required = false) String token) {
        List<TarefaDTO> tarefas = tarefaService.buscaTarefasAgendadasPorUsuario(token);
        return ResponseEntity.ok(tarefas);
    }

    /**
     * Endpoint específico mapeado em /email para evitar colisão com o GET raiz de listagem de tarefas.
     */
    @GetMapping("/email")
    @Operation(summary = "Busca tarefas por e-mail", description = "Busca tarefas filtrando pelo e-mail do usuário")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<TarefaDTO>> buscaTarefaPorEmail(@RequestParam("email") String email,
                                                               @RequestHeader(name = "Authorization", required = false) String token) {
        List<TarefaDTO> tarefas = tarefaService.buscaTarefaPorEmail(email, token);
        return ResponseEntity.ok(tarefas);
    }

    /**
     * Deleta uma tarefa específica com base no parâmetro 'id' passado na query string.
     */
    @DeleteMapping
    @Operation(summary = "Deleta tarefa por ID", description = "Remove uma tarefa com base no ID informado")
    @ApiResponse(responseCode = "200", description = "Tarefa deletada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> deletaTarefaPorId(@RequestParam("id") String id,
                                                  @RequestHeader(name = "Authorization", required = false) String token) {
        tarefaService.deletaTarefaPorId(id, token);
        return ResponseEntity.ok().build();
    }

    /**
     * Altera de forma parcial (PATCH) o status de notificação de uma tarefa.
     */
    @PatchMapping
    @Operation(summary = "Altera status de notificação", description = "Atualiza o status de notificação de uma tarefa")
    @ApiResponse(responseCode = "200", description = "Status alterado com sucesso")
    public ResponseEntity<TarefaDTO> alteraStatusNotificacao(@RequestParam("status") StatusNotificacaoEnum status,
                                                             @RequestParam("id") String id,
                                                             @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.alteraStatus(status, id, token));
    }

    /**
     * Atualiza os dados completos de uma tarefa existente através do verbo PUT.
     */
    @PutMapping
    @Operation(summary = "Atualiza tarefa", description = "Atualiza os dados de uma tarefa existente")
    @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso")
    public ResponseEntity<TarefaDTO> atualizaTarefa(@RequestBody TarefaDTO dto,
                                                    @RequestParam("id") String id,
                                                    @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.atualizaTarefa(dto, id, token));
    }


}
