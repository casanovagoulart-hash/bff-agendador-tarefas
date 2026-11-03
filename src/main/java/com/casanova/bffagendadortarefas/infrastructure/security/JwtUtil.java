package com.casanova.bffagendadortarefas.infrastructure.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    // Injeta a chave secreta configurada no application.properties (obtida de forma segura via variável de ambiente)
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Responsável por gerar a chave criptográfica (SecretKey) seguindo a especificação RFC 7518 (HS512).
     * Converte a string secreta em um array de bytes utilizando UTF-8 e instancia a chave segura
     * para assinatura e validação dos tokens JWT.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
