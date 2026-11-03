package com.casanova.bffagendadortarefas.business;

import com.casanova.bffagendadortarefas.business.dto.EnderecoDTO;
import com.casanova.bffagendadortarefas.business.dto.TelefoneDTO;
import com.casanova.bffagendadortarefas.business.dto.UsuarioDTO;
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
    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        return client.salvaUsuario(usuarioDTO).getBody();
    }

    /**
     * Realiza a autenticação do usuário, repassando as credenciais e retornando o token JWT.
     */
    public String loginUsuario(UsuarioDTO usuarioDTO) {
        return client.login(usuarioDTO);
    }

    /**
     * Atualiza os dados cadastrais do usuário autenticado, validando via token JWT.
     */
    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        return client.atualizaDadosUsuario(dto, token);
    }

    /**
     * Busca os dados de um usuário específico filtrando por e-mail (requer token de acesso).
     */

    public UsuarioDTO buscaUsuarioporEmail(String email, String token) {
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
    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO, String token) {
        return client.atualizaEndereco(enderecoDTO, idEndereco, token);
    }

    /**
     * Atualiza as informações de um telefone específico do usuário através do seu ID.
     */
    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto, String token) {
        return client.atualizaTelefone(dto, idTelefone, token);
    }

}
