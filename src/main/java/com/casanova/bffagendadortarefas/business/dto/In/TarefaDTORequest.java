package com.casanova.bffagendadortarefas.business.dto.In;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TarefaDTORequest {

    private String nomeTarefa;

    // CORREÇÃO: Ajustado de 'descrIcao' para 'descricao'.
    // O Jackson usa o nome exato do atributo para gerar a chave no JSON;
    // a letra maiúscula fazia o microsserviço Core receber o campo como null.
    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataEvento;
}
