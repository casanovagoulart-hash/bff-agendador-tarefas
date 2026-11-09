package com.casanova.bffagendadortarefas.infrastructure.client;

import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao-service", url = "${notificacao.url}")
public interface EmailClient {

    @PostMapping
    void enviarEmail(@RequestBody TarefaDTOResponse dto);

}

