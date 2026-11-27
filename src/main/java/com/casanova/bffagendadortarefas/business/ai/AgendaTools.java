package com.casanova.bffagendadortarefas.business.ai;

import com.casanova.bffagendadortarefas.business.TarefaService;
import com.casanova.bffagendadortarefas.business.dto.In.TarefaDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AgendaTools {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final TarefaService tarefaService;
    private final String tokenJwt;

    @Tool(description = "Retorna a data e hora atual do servidor no formato dd-MM-yyyy HH:mm:ss para calcular datas relativas como 'amanhã' ou 'hoje'")
    public String obterDataHoraAtual() {
        return LocalDateTime.now().format(FORMATTER);
    }

    @Tool(description = "Agenda uma nova tarefa para o usuário autenticado")
    public String criarTarefa(
            @ToolParam(description = "Título curto da tarefa") String nomeTarefa,
            @ToolParam(description = "Descrição detalhada da tarefa") String descricao,
            @ToolParam(description = "Data e hora do evento no formato exato dd-MM-yyyy HH:mm:ss") String dataEvento) {

        try {
            log.info("Agente IA criando tarefa: '{}' para {}", nomeTarefa, dataEvento);

            // Normaliza barras caso o modelo envie dd/MM/yyyy em vez de dd-MM-yyyy
            String dataNormalizada = dataEvento.replace("/", "-").trim();

            TarefaDTORequest request = TarefaDTORequest.builder()
                    .nomeTarefa(nomeTarefa)
                    .descricao(descricao)
                    .dataEvento(LocalDateTime.parse(dataNormalizada, FORMATTER))
                    .build();

            TarefaDTOResponse salva = tarefaService.gravaTarefa(tokenJwt, request);
            return "Tarefa agendada com sucesso! ID: " + salva.getId() + " | Evento: " + salva.getDataEvento();
        } catch (Exception e) {
            log.error("Falha ao executar ferramenta criarTarefa: {}", e.getMessage());
            return "Erro ao agendar tarefa: " + e.getMessage() + ". Verifique o formato dd-MM-yyyy HH:mm:ss.";
        }
    }
}
