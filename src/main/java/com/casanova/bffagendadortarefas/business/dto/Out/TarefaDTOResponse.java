package com.casanova.bffagendadortarefas.business.dto.Out;

import com.casanova.bffagendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TarefaDTOResponse {

    private String id;
    private String nomeTarefa;

    // CORREÇÃO: Padronizado para 'descricao' (mesmo motivo do Request)
    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataEvento;

    // CORREÇÃO: Removido @JsonFormat deste campo, pois 'emailUsuario' é String e não data
    private String emailUsuario;

    // CORREÇÃO: Corrigido o typo 'dataAteracao' -> 'dataAlteracao' e aplicada a máscara de data
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataAlteracao;

    // CORREÇÃO: Removido @JsonFormat deste campo, pois é um Enum
    private StatusNotificacaoEnum statusNotificacaoEnum;

    public void setEmail(String email) {
        this.emailUsuario = email;
    }
}
