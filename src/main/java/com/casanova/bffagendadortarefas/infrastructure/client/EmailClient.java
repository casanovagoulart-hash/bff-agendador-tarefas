package com.casanova.bffagendadortarefas.infrastructure.client;

import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTOResponse;
import com.casanova.bffagendadortarefas.business.dto.Out.ViaCepDTOResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao-service", url = "${notificacao.url}")
public interface EmailClient {

    @PostMapping("/email") // Alinhado com @RequestMapping("/email") do EmailController
    void enviarEmail(@RequestBody TarefaDTOResponse dto);


}

