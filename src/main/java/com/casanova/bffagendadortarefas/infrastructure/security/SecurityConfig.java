package com.casanova.bffagendadortarefas.infrastructure.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Agenda de Tarefas API", version = "v1"))
@EnableWebSecurity
@SecurityScheme(name = SecurityConfig.SEGURITY_SCHEME, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SecurityConfig {

    public static final String SEGURITY_SCHEME = "bearerAuth";


@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa CSRF para APIs stateless
                .authorizeHttpRequests(auth -> auth
                        // 1. Libera a interface gráfica do Swagger UI e metadados OpenAPI sem pedir HTTP Basic
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 2. Libera endpoints públicos de login e cadastro do usuário
                        .requestMatchers("/usuario/login", "/usuario").permitAll()

                        // 3. Exige autenticação apenas para as demais rotas de negócio (ex: /tarefas/**)
                        .anyRequest().authenticated()
                );

        return http.build();
    }





}
