package com.casanova.bffagendadortarefas.controller;

import com.casanova.bffagendadortarefas.business.TarefaService;
import com.casanova.bffagendadortarefas.business.ai.AgendaTools;
import com.casanova.bffagendadortarefas.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assistente")
@Tag(name = "Assistente IA", description = "Agente inteligente para gestão de tarefas via linguagem natural")
@SecurityRequirement(name = SecurityConfig.SEGURITY_SCHEME)
public class AssistenteController {

    private final ChatClient chatClient;
    private final TarefaService tarefaService;

    public AssistenteController(ChatClient.Builder builder, TarefaService tarefaService) {
        this.tarefaService = tarefaService;
        this.chatClient = builder
                .defaultSystem("""
                        Você é um assistente pessoal de produtividade integrado ao Agendador de Tarefas.
                        Sempre chame 'obterDataHoraAtual' antes de calcular datas relativas como 'amanhã', 'hoje' ou 'daqui a 1 hora'.
                        Seja direto, educado e confirme os dados da tarefa após agendá-la ou listá-la.
                        """)
                .build();
    }

    @PostMapping("/chat")
    @Operation(summary = "Conversar com o Agente de IA", description = "Permite criar e consultar tarefas em linguagem natural")
    public ResponseEntity<String> conversar(
            @RequestBody String mensagem,
            @RequestHeader(name = "Authorization", required = false) String token){

        AgendaTools toolsDoUsuario = new AgendaTools(tarefaService, token);

        String resposta = chatClient.prompt()
                .user(mensagem)
                .tools(toolsDoUsuario)
                .call()
                .content();

        return ResponseEntity.ok(resposta);
    }
}
