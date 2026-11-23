package com.casanova.bffagendadortarefas.business.dto.Out;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnderecoDTOResponse {

    private Long id;
    private String rua;
    private Long numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
