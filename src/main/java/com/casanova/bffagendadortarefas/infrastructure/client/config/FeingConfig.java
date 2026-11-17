package com.casanova.bffagendadortarefas.infrastructure.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FeingConfig {

    @Bean
    public FeingError feingError() {
        return new FeingError();
    }


}
