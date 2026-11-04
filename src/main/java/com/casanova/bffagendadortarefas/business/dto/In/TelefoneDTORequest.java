package com.casanova.bffagendadortarefas.business.dto.In;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TelefoneDTORequest {


    private String ddd;
    private String numero;
}
