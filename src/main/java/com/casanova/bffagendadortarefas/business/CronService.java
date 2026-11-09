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

    /**
     * Executa periodicamente conforme a expressão cron definida no application.properties.
     * Busca tarefas agendadas para a próxima hora e dispara as notificações por e-mail.
     */
    @Scheduled(cron = "${cron.horario}")
    public void buscaTarefasProximaHora() {
        try {
            log.info("Iniciando o processo de busca de tarefas agendadas para a próxima hora.");

            // Realiza o login institucional para obter o token JWT de sistema
            String token = login(converterPararequestDTO());

            LocalDateTime horaAtual = LocalDateTime.now();
            LocalDateTime horaFutura = horaAtual.plusHours(1);

            // Busca as tarefas pendentes no intervalo estipulado
            List<TarefaDTOResponse> listaTarefas = tarefaService.buscaTarefasAgendadasPorPeriodo(horaAtual, horaFutura, token);

            if (listaTarefas == null || listaTarefas.isEmpty()) { // Correção defensiva para null check
                log.info("Nenhuma tarefa encontrada para o período atual.");
                return;
            }

            log.info("Total de tarefas encontradas para notificação: {}", listaTarefas.size());

            // Processa cada tarefa encontrada
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
            log.info("Processamento de notificações e busca de tarefas.");

        } catch (Exception e) {
            log.error("Erro crítico durante a execução do agendamento de tarefas (CronService). Detalhes: {}", e.getMessage(), e);
        }
    }

    /**
     * Realiza a autenticação para obtenção do token JWT.
     */
    public String login(LoginRequestDTO dto) {
        return tarefaService.loginUsuario(dto);
    }

    /**
     * Converte as propriedades injetadas em um DTO de requisição de login.
     */
    public LoginRequestDTO converterPararequestDTO() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail(email);
        dto.setSenha(senha);
        return dto;
    }
}
