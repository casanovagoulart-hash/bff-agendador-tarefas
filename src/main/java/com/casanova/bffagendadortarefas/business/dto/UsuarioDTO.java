package com.casanova.bffagendadortarefas.business.dto;;

import lombok.*;

import java.util.List;

// Lombok: gera getters, setters, construtores e builder automaticamente
// @NoArgsConstructor: o Jackson precisa de um construtor vazio para montar o objeto a partir do JSON
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
}
