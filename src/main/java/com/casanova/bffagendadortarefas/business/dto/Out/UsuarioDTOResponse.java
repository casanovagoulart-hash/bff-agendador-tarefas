package com.casanova.bffagendadortarefas.business.dto.Out;

import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTOResponse {

    // CORREÇÃO: Alterado de String para Long para espelhar a chave numérica do PostgreSQL
    private Long id;
    private String nome;
    private String email;

    // SEGURANÇA: Removido o campo 'senha' para nunca expor hash na resposta da API
    // COMPLETUDE: Adicionadas as listas para o BFF retornar o perfil completo do usuário
    private List<EnderecoDTOResponse> enderecos;
    private List<TelefoneDTOResponse> telefones;
}
