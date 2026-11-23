package com.casanova.bffagendadortarefas.controller;

// Oculta este controlador da documentação pública do Swagger UI (evita alertar curiosos)
import io.swagger.v3.oas.annotations.Hidden;
// Representa a requisição HTTP recebida, permitindo extrair IP, cabeçalhos e rota atacada
import jakarta.servlet.http.HttpServletRequest;
// Gera automaticamente o objeto 'log' (SLF4J) para registrar os alertas no console/arquivo
import lombok.extern.slf4j.Slf4j;
// Enum com os códigos de resposta HTTP padrão (usamos 403 Forbidden para bloquear o invasor)
import org.springframework.http.HttpStatus;
// Encapsula o corpo e o status HTTP devolvidos ao cliente
import org.springframework.http.ResponseEntity;
// Mapeia requisições HTTP (GET, POST, PUT, DELETE) e registra a classe como endpoint REST
import org.springframework.web.bind.annotation.*;

// Captura a data e hora exatas do ataque para a linha do tempo forense
import java.time.LocalDateTime;
// Formata o timestamp em padrão legível para auditoria (ano-mês-dia hora:minuto:segundo)
import java.time.format.DateTimeFormatter;
// Converte a Enumeration antiga de cabeçalhos do Servlet em uma lista moderna manipulável
import java.util.Collections;
// Concatena todos os cabeçalhos HTTP em uma única linha organizada para o log
import java.util.stream.Collectors;

@Slf4j
@Hidden
@RestController
public class HoneypotController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Endpoint armadilha (Honeypot): aceita qualquer verbo HTTP em rotas comumente
     * buscadas por bots e scanners de vulnerabilidade, gravando toda a assinatura do ataque.
     */
    @RequestMapping(value = {
            "/admin",
            "/admin/login",
            "/api/admin/backup",
            "/api/v1/users/export",
            "/actuator/env",
            "/.env"
    })
    public ResponseEntity<String> armadilhaInvasor(
            HttpServletRequest request,
            @RequestBody(required = false) String payload) {

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String ipOrigem = extrairIpReal(request);
        String metodo = sanitizar(request.getMethod());
        String rota = sanitizar(request.getRequestURI());
        String query = sanitizar(request.getQueryString());
        String userAgent = sanitizar(request.getHeader("User-Agent"));

        // Limita o corpo capturado a 300 caracteres para evitar ataques de exaustão de disco/log
        String payloadLimpo = (payload != null && !payload.isBlank())
                ? sanitizar(payload.substring(0, Math.min(payload.length(), 300)))
                : "VAZIO";

        // Coleta todos os cabeçalhos enviados (ferramenta de ataque, cookies, tokens falsos, etc.)
        String headers = Collections.list(request.getHeaderNames()).stream()
                .map(h -> sanitizar(h) + "=" + sanitizar(request.getHeader(h)))
                .collect(Collectors.joining(" | "));

        log.error("""
                
                !!!!!!!!!!!!!!!!!!!! [HONEYPOT ACIONADO - INVASÃO DETECTADA] !!!!!!!!!!!!!!!!!!!!
                Data/Hora      : {}
                IP do Atacante : {}
                Alvo Tocado    : {} {} (Query: {})
                Dispositivo/UA : {}
                Payload Enviado: {}
                Headers HTTP   : {}
                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!""",
                timestamp, ipOrigem, metodo, rota, query, userAgent, payloadLimpo, headers
        );

        // Resposta genérica: bloqueia o acesso sem revelar que o endpoint é uma armadilha
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access Denied: Security violation logged.");
    }

    /**
     * Identifica o IP real do solicitante. Quando há proxy reverso ou CDN (Nginx, Cloudflare),
     * o cabeçalho 'X-Forwarded-For' traz a cadeia "ip_cliente, ip_proxy1", onde [0] é a origem real.
     */
    private String extrairIpReal(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return sanitizar(forwarded.split(",")[0].trim());
        }
        return sanitizar(request.getRemoteAddr());
    }

    /**
     * Defesa contra Log Injection (CRLF): remove quebras de linha (\r e \n) enviadas
     * em cabeçalhos maliciosos para impedir a falsificação de registros no log.
     */
    private String sanitizar(String valor) {
        if (valor == null) return "N/A";
        return valor.replaceAll("[\\r\\n]", "_");
    }
}
