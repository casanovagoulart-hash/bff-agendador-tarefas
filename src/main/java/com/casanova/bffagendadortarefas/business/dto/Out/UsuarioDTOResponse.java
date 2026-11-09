package com.casanova.bffagendadortarefas.business.dto.Out;

import lombok.*;

// Lombok: gera getters, setters, construtores e builder automaticamente
// @NoArgsConstructor: o Jackson precisa de um construtor vazio para montar o objeto a partir do JSON
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTOResponse {

    private String nome;
    private String email;
    private String senha;
}
