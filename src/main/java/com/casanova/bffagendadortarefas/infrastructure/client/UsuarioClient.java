package com.casanova.bffagendadortarefas.infrastructure.client;

import com.casanova.bffagendadortarefas.business.dto.EnderecoDTO;
import com.casanova.bffagendadortarefas.business.dto.TelefoneDTO;
import com.casanova.bffagendadortarefas.business.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "usuario-service", contextId = "usuarioClient", url = "${usuario.url}")
public interface UsuarioClient {

// O retorno agora é UsuarioDTO (e não UsuarioService)
@GetMapping("/usuario")
UsuarioDTO buscaUsuarioPorEmail(@RequestParam("email") String email,
                                @RequestHeader("Authorization") String token);

    @PostMapping("/usuario")
    ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO);

    // @PostMapping("/login"): atende POST /usuario/login (rota já liberada na SecurityConfig)
    // LOGIN: é aqui que o token NASCE.
    // 1) authenticate confere e-mail e senha (usa o UserDetailsServiceImpl e o BCrypt)
    // 2) se estiver certo, generateToken coloca o e-mail do usuário DENTRO do token
    //    (campo "subject") e assina com a chave secreta que só o servidor conhece
    // 3) Cada login gera um token novo, e todos funcionam até expirar
    // Atenção: "Bearer " (com espaço) NÃO faz parte do token, é só o tipo de autenticação.
    // O token de verdade é o texto longo que vem depois (eyJ...).
    @PostMapping("/usuario/login")
    String login(@RequestBody UsuarioDTO usuarioDTO);

    // @PutMapping: atende PUT /usuario (PUT é o verbo usado para ATUALIZAR dados que já existem)
    // Não recebe id nem e-mail na URL de propósito: quem decide QUEM é o usuário é o token.
    // Se o id viesse nos Params, qualquer usuário logado poderia trocar o número e alterar
    // os dados de outra pessoa (falha de segurança chamada IDOR).
    @PutMapping("/usuario")
    UsuarioDTO atualizaDadosUsuario(
            // @RequestBody: transforma o JSON enviado no Apidog em um UsuarioDTO
            @RequestBody UsuarioDTO dto,
            // @RequestHeader("Authorization"): pega o "Bearer eyJ..." do cabeçalho da requisição
            @RequestHeader("Authorization") String token);

    // @PutMapping("/endereco"): atende PUT /usuario/endereco?id=1
    // Aqui o id nos Params FAZ sentido: um usuário pode ter vários endereços,
    // então precisamos dizer QUAL deles será atualizado.
    @PutMapping("/usuario/endereco")
    EnderecoDTO atualizaEndereco(
            // @RequestBody: transforma o JSON do Body em um EnderecoDTO
            // (só os campos enviados mudam, os nulos mantêm o valor do banco)
            @RequestBody EnderecoDTO dto,
            // @RequestParam("id"): pega o id que vem na URL, depois do "?" (aba Params do Apidog)
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String token);

    // Mesma ideia do endereço, agora para o telefone: PUT /usuario/telefone?id=1
    @PutMapping("/usuario/telefone")
    TelefoneDTO atualizaTelefone(
            @RequestBody TelefoneDTO dto,
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String token);

    // @PathVariable: pega o e-mail que vem na própria URL (/usuario/{email})
    @DeleteMapping("/usuario/{email}")
    void deletaUsuarioPorEmail(@PathVariable String email,
                               @RequestHeader("Authorization") String token);


}
