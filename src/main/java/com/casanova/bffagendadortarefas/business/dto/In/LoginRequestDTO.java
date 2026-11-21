package com.casanova.bffagendadortarefas.business.dto.In;

import lombok.*;

// CORREÇÃO: Removido 'extends UsuarioDTOResponse'.
// Herdar uma classe de saída e redeclarar os mesmos atributos cria "sombreamento" (shadowing):
// existem dois campos 'email' e 'senha' na memória, fazendo com que o Jackson ou os getters
// leiam valores nulos dependendo de qual referência for acessada.
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {

    private String email;
    private String senha;
}
