package com.casanova.bffagendadortarefas.business.dto.In;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnderecoDTORequest {


    private String rua;
    private Long numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
