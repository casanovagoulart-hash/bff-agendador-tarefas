package com.casanova.bffagendadortarefas.business.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnderecoDTO {

    private Long id;
    private String rua;
    private Long numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
