package com.casanova.bffagendadortarefas.business;

import com.casanova.bffagendadortarefas.business.dto.In.EnderecoDTORequest;
import com.casanova.bffagendadortarefas.business.dto.In.LoginRequestDTO;
import com.casanova.bffagendadortarefas.business.dto.In.TelefoneDTORequest;
import com.casanova.bffagendadortarefas.business.dto.In.UsuarioDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.EnderecoDTOResponse;
import com.casanova.bffagendadortarefas.business.dto.Out.TelefoneDTOResponse;
import com.casanova.bffagendadortarefas.business.dto.Out.UsuarioDTOResponse;
import com.casanova.bffagendadortarefas.infrastructure.client.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    // Cliente Feign responsável por se comunicar com o microsserviço de usuários
    private final UsuarioClient client;

    /**
     * Encaminha os dados para o microsserviço criar e salvar um novo usuário no sistema.
     */
    public UsuarioDTOResponse salvaUsuario(UsuarioDTORequest usuarioDTO) {
        return client.salvaUsuario(usuarioDTO).getBody();
    }

    /**
     * Realiza a autenticação do usuário, repassando as credenciais e retornando o token JWT.
     */
    public String loginUsuario(LoginRequestDTO dto) {
        return client.login(dto);
    }

    /**
     * Atualiza os dados cadastrais do usuário autenticado, validando via token JWT.
     */
    public UsuarioDTOResponse atualizaDadosUsuario(String token, UsuarioDTORequest dto) {
        return client.atualizaDadosUsuario(dto, token);
    }

    /**
     * Busca os dados de um usuário específico filtrando por e-mail (requer token de acesso).
     */

    public UsuarioDTOResponse buscaUsuarioPorEmail(String email, String token) {
        return client.buscaUsuarioPorEmail(email, token);
    }

    /**
     * Solicita ao microsserviço a exclusão de um usuário com base no e-mail informado.
     */

    public void deleteUsuarioPorEmail(String email, String token) {
        client.deletaUsuarioPorEmail(email, token);
    }

    /**
     * Atualiza as informações de um endereço específico do usuário através do seu ID.
     */
    public EnderecoDTOResponse atualizaEndereco(Long idEndereco, EnderecoDTORequest enderecoDTO, String token) {
        return client.atualizaEndereco(enderecoDTO, idEndereco, token);
    }

    /**
     * Atualiza as informações de um telefone específico do usuário através do seu ID.
     */
    public TelefoneDTOResponse atualizaTelefone(Long idTelefone, TelefoneDTORequest dto, String token) {
        return client.atualizaTelefone(dto, idTelefone, token);
    }

}
