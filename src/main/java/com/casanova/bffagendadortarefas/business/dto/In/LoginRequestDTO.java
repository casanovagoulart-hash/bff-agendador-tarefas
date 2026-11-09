package com.casanova.bffagendadortarefas.business.dto.In;

import com.casanova.bffagendadortarefas.business.dto.Out.UsuarioDTOResponse;
import lombok.*;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class LoginRequestDTO extends UsuarioDTOResponse {

    private String email;
    private String senha;

}
