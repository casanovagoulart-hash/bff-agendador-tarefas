// CORREÇÃO: Removido o ponto e vírgula duplicado no final do package
package com.casanova.bffagendadortarefas.business.dto.In;

import lombok.*;

// CORREÇÃO: Removido 'extends UsuarioDTOResponse' para eliminar o sombreamento de atributos
// e manter o isolamento arquitetural entre DTOs de entrada (In) e saída (Out).
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTORequest {

    private String nome;
    private String email;
    private String senha;
}
