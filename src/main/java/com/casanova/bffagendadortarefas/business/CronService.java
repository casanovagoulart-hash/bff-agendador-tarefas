package com.casanova.bffagendadortarefas.business;

import com.casanova.bffagendadortarefas.business.dto.In.LoginRequestDTO;
import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
import com.casanova.bffagendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronService {

    private final TarefaService tarefaService;
    private final EmailService emailService;
    private final UsuarioService usuarioService;

    @Value("${usuario.email}")
    private String email;

    @Value("${usuario.senha}")
    private String senha;

    @Scheduled(cron = "${cron.horario}")
    public void buscaTarefasProximaHora() {
        try {
            log.info("Iniciando o processo de busca de tarefas agendadas para a próxima hora.");

            // CORREÇÃO 1: O login é realizado no UsuarioService (microsserviço de usuários).
            // CORREÇÃO 2: Adicionado o prefixo "Bearer " caso o endpoint de login retorne apenas o hash JWT,
            // pois o JwtFilter exige "Bearer " no início do cabeçalho Authorization.
            String rawToken = login(converterPararequestDTO());
            String token = rawToken.startsWith("Bearer ") ? rawToken : "Bearer " + rawToken;

            LocalDateTime horaAtual = LocalDateTime.now();
            LocalDateTime horaFutura = horaAtual.plusHours(1);

            List<TarefaDTOResponse> listaTarefas = tarefaService.buscaTarefasAgendadasPorPeriodo(horaAtual, horaFutura, token);

            if (listaTarefas == null || listaTarefas.isEmpty()) {
                log.info("Nenhuma tarefa encontrada para o período atual.");
                return;
            }

            log.info("Total de tarefas encontradas para notificação: {}", listaTarefas.size());

            listaTarefas.forEach(tarefa -> {
                try {
                    emailService.enviaEmail(tarefa);
                    log.info("E-mail de notificação enviado com sucesso para o usuário: {}", tarefa.getEmailUsuario());

                    tarefaService.alteraStatus(StatusNotificacaoEnum.NOTIFICADO, tarefa.getId(), token);
                    log.info("Status da tarefa ID: {} atualizado para NOTIFICADO.", tarefa.getId());
                } catch (Exception e) {
                    log.error("Erro ao processar a notificação da tarefa ID: {}. Detalhes: {}", tarefa.getId(), e.getMessage());
                }
            });

            log.info("Processamento de notificações concluído.");

        } catch (Exception e) {
            log.error("Erro crítico durante a execução do agendamento de tarefas (CronService). Detalhes: {}", e.getMessage(), e);
        }
    }

    public String login(LoginRequestDTO dto) {
        // CORREÇÃO: Direcionado para o UsuarioService que já estava injetado nesta classe
        return usuarioService.loginUsuario(dto);
    }

    public LoginRequestDTO converterPararequestDTO() {
        return LoginRequestDTO.builder()
                .email(email)
                .senha(senha)
                .build();
    }
}
