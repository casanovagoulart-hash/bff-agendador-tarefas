package com.casanova.bffagendadortarefas.business.dto.In;;

import com.casanova.bffagendadortarefas.business.dto.Out.UsuarioDTOResponse;
import lombok.*;

// Lombok: gera getters, setters, construtores e builder automaticamente
// @NoArgsConstructor: o Jackson precisa de um construtor vazio para montar o objeto a partir do JSON

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTORequest extends UsuarioDTOResponse {


    private String nome;
    private String email;
    private String senha;
}
