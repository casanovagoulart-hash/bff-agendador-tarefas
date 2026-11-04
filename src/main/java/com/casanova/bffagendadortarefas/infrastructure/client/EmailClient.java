/* package com.casanova.bffagendadortarefas.infrastructure.client;

import com.casanova.bffagendadortarefas.business.dto.Out.TarefaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao", contextId = "notificacao", url = "${notificacao.url}")
public interface EmailClient {

void enviarEmail(@RequestBody TarefaDTO dto);


}
*/