package com.casanova.bffagendadortarefas.controller;

import com.casanova.bffagendadortarefas.business.TarefaService;
import com.casanova.bffagendadortarefas.business.dto.In.TarefaDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
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

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Gerenciamento e cadastro de tarefas de usuários")
@SecurityRequirement(name = SecurityConfig.SEGURITY_SCHEME)
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    @Operation(summary = "Salvar Tarefa", description = "Cria uma nova tarefa para o usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Tarefa salva com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    @ApiResponse(responseCode = "403", description = "Tarefa não salva.")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")
    public ResponseEntity<TarefaDTOResponse> gravaTarefa(@RequestBody TarefaDTORequest dto,
                                                         @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.gravaTarefa(token, dto));
    }

    @GetMapping("/eventos")
    @Operation(summary = "Busca tarefas por período", description = "Busca tarefas cadastradas em um intervalo de datas")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    @ApiResponse(responseCode = "403", description = "Período não encontrado.")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")
    public ResponseEntity<List<TarefaDTOResponse>> buscaTarefasAgendadasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal, token));
    }

    @GetMapping
    @Operation(summary = "Busca tarefas do usuário", description = "Busca todas as tarefas agendadas do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")
    public ResponseEntity<List<TarefaDTOResponse>> buscaTarefasAgendadasPorUsuario(@RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.buscaTarefasAgendadasPorUsuario(token));
    }

    @GetMapping("/email")
    @Operation(summary = "Busca tarefas por e-mail", description = "Busca tarefas filtrando pelo e-mail do usuário")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @ApiResponse(responseCode = "403", description = "E-mail não encontrado.")
    // CORREÇÃO: Removidos espaços extras em "401" para exibição correta no Swagger UI
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")
    public ResponseEntity<List<TarefaDTOResponse>> buscaTarefaPorEmail(@RequestParam("email") String email,
                                                                       @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.buscaTarefaPorEmail(email, token));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta tarefa por ID", description = "Remove uma tarefa com base no ID informado na URL")
    @ApiResponse(responseCode = "200", description = "Tarefa deletada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @ApiResponse(responseCode = "403", description = "Tarefa id não encontrada.")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")
    public ResponseEntity<Void> deletaTarefaPorId(@PathVariable("id") String id,
                                                  @RequestHeader(name = "Authorization", required = false) String token) {
        tarefaService.deletaTarefaPorId(id, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Altera status de notificação", description = "Atualiza o status de notificação de uma tarefa pelo ID")
    @ApiResponse(responseCode = "200", description = "Status alterado com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    @ApiResponse(responseCode = "403", description = "Tarefa id não encontrada.")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")
    public ResponseEntity<TarefaDTOResponse> alteraStatusNotificacao(@RequestParam("status") StatusNotificacaoEnum status,
                                                                     @PathVariable("id") String id,
                                                                     @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.alteraStatus(status, id, token));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza tarefa", description = "Atualiza os dados de uma tarefa existente pelo ID")
    @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    @ApiResponse(responseCode = "403", description = "Tarefa id não encontrada.")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")
    // CORREÇÃO: Alterado o @RequestBody de TarefaDTOResponse para TarefaDTORequest
    public ResponseEntity<TarefaDTOResponse> atualizaTarefa(@RequestBody TarefaDTORequest dto,
                                                            @PathVariable("id") String id,
                                                            @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefaService.atualizaTarefa(dto, id, token));
    }
}
