package com.casanova.bffagendadortarefas.business.dto.Out;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TelefoneDTOResponse {

    private Long id;
    private String ddd;
    private String numero;
}
