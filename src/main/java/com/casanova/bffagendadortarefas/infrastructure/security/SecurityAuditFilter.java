package com.casanova.bffagendadortarefas.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// CIBERSEGURANÇA:
// @Order(Ordered.HIGHEST_PRECEDENCE) garante que este filtro envolva toda a cadeia
// do Spring Security, capturando o status final da resposta mesmo quando o bloqueio
// acontece antes de chegar aos Controllers (ex: token JWT inválido ou ausente).
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityAuditFilter extends OncePerRequestFilter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Deixa a requisição seguir pelo fluxo normal (JWT, Controllers, Feign)
            filterChain.doFilter(request, response);
        } finally {
            int status = response.getStatus();

            // Monitora tentativas de acesso não autorizado (401), proibido (403)
            // ou varredura de rotas ocultas (404)
            if (status == HttpServletResponse.SC_UNAUTHORIZED || status == HttpServletResponse.SC_FORBIDDEN) {
                registrarEventoSeguranca(request, status);
            }
        }
    }

    private void registrarEventoSeguranca(HttpServletRequest request, int status) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String ipOrigem = extrairIpReal(request);
        String metodo = sanitizar(request.getMethod());
        String rota = sanitizar(request.getRequestURI());
        String userAgent = sanitizar(request.getHeader("User-Agent"));
        String authHeader = request.getHeader("Authorization");

        // NUNCA grave o token inteiro no log (evita vazamento de credencial ativa).
        // Registramos apenas se houve tentativa com Bearer ou tentativa anônima.
        String tipoCredencial = (authHeader != null && authHeader.startsWith("Bearer "))
                ? "TOKEN_JWT_APRESENTADO (Recusado/Expirado)"
                : "SEM_TOKEN_OU_LOGIN_INVALIDO";

        log.warn("""
                
                ==================== [ALERTA DE AUDITORIA DE SEGURANÇA] ====================
                Data/Hora      : {}
                Status HTTP    : {} ({})
                IP de Origem   : {}
                Rota Alvo      : {} {}
                Credencial     : {}
                Dispositivo/UA : {}
                ============================================================================""",
                timestamp,
                status,
                status == 401 ? "UNAUTHORIZED" : "FORBIDDEN",
                ipOrigem,
                metodo,
                rota,
                tipoCredencial,
                userAgent
        );
    }

    /**
     * Extrai o IP verdadeiro do cliente mesmo se houver proxy reverso, Load Balancer ou Cloudflare.
     */
    private String extrairIpReal(HttpServletRequest request) {
        String[] headersProxy = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };

        for (String header : headersProxy) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
                // CORREÇÃO: [0] pega o primeiro IP da lista antes de aplicar .trim()
                return sanitizar(ip.split(",")[0].trim());
            }
        }
        return sanitizar(request.getRemoteAddr());
    }


    /**
     * DEFESA CONTRA LOG INJECTION (CRLF):
     * Remove quebras de linha (\r e \n) para impedir que um atacante manipule cabeçalhos HTTP
     * e forje entradas falsas no arquivo de auditoria.
     */
    private String sanitizar(String valor) {
        if (valor == null) {
            return "DESCONHECIDO";
        }
        return valor.replaceAll("[\\r\\n]", "_");
    }
}
