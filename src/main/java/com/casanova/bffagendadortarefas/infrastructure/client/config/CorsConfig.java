package com.casanova.bffagendadortarefas.infrastructure.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // Whitelist explícita: libera apenas o front-end autorizado
                        .allowedOrigins("http://localhost:4200", "http://localhost:3000")
                        // Verbos separados individualmente + OPTIONS (obrigatório para preflight)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // Cabeçalhos necessários para trafegar JSON e o Bearer Token JWT
                        .allowedHeaders("Authorization", "Content-Type", "Accept", "Origin")
                        .exposedHeaders("Authorization")
                        .allowCredentials(true)
                        // Cache da verificação preflight por 1 hora (3600 segundos)
                        .maxAge(3600);
            }
        };
    }
}
