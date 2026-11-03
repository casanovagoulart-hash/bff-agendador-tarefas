package com.casanova.bffagendadortarefas.business.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TelefoneDTO {

    private Long id;
    private String ddd;
    private String numero;
}
