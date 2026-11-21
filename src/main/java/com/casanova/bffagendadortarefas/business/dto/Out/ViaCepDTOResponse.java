package com.casanova.bffagendadortarefas.business.dto.Out;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ViaCepDTOResponse {

        // CORREÇÃO: Alterados de 'public' para 'private' para respeitar o encapsulamento,
        // já que o Lombok (@Getter / @Setter) já gera os métodos de acesso.
        private String cep;
        private String logradouro;
        private String complemento;
        private String unidade;
        private String bairro;
        private String localidade;
        private String uf;
        private String estado;
        private String regiao;
        private String ibge;
        private String gia;
        private String ddd;
        private String siafi;
}
