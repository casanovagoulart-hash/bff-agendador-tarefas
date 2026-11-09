package com.casanova.bffagendadortarefas.controller;

import com.casanova.bffagendadortarefas.business.UsuarioService;
import com.casanova.bffagendadortarefas.business.dto.In.EnderecoDTORequest;
import com.casanova.bffagendadortarefas.business.dto.In.LoginRequestDTO;
import com.casanova.bffagendadortarefas.business.dto.In.TelefoneDTORequest;
import com.casanova.bffagendadortarefas.business.dto.In.UsuarioDTORequest;
import com.casanova.bffagendadortarefas.business.dto.Out.EnderecoDTOResponse;
import com.casanova.bffagendadortarefas.business.dto.Out.TelefoneDTOResponse;
import com.casanova.bffagendadortarefas.business.dto.Out.UsuarioDTOResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Cadastro, Login e Usuário")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Endpoint responsável por cadastrar um novo usuário no sistema.
     */
    @PostMapping
    @Operation(summary = "Salvar Usuários", description = "Cria um novo usuário")
    @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<UsuarioDTOResponse> salvaUsuario(@RequestBody UsuarioDTORequest dtoRequest) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(dtoRequest));
    }

    /**
     * Endpoint para realizar o login e gerar o token de acesso JWT.
     */
    @PostMapping("/login")
    @Operation(summary = "Login Usuários", description = "Realiza o login do usuário")
    @ApiResponse(responseCode = "200", description = "Logado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.loginUsuario(usuarioDTO));
    }

    /**
     * Busca um usuário pelo e-mail informado, validando através do token enviado no header.
     */
    @GetMapping
    @Operation(summary = "Buscar Usuários", description = "Busca um usuário pelo e-mail")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<UsuarioDTOResponse> buscaUsuarioPorEmail(@RequestParam("email") String email,
                                                                  @RequestHeader(name = "Authorization", required = false) String token) {
        // Passa o email e o token para o service satisfazer os 2 argumentos esperados[cite: 4]
        return ResponseEntity.ok(usuarioService.buscaUsuarioPorEmail(email, token));
    }

    /**
     * Atualiza os dados cadastrais gerais do usuário logado.
     */
    @PutMapping
    @Operation(summary = "Atualizar Usuários", description = "Atualiza os dados de um usuário")
    @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<UsuarioDTOResponse> atualizaDadosUsuario(
            @RequestBody UsuarioDTORequest dto,
            @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }

    /**
     * Atualiza um endereço específico com base no ID informado.
     */
    @PutMapping("/endereco")
    @Operation(summary = "Atualizar Endereços", description = "Atualiza os dados de um endereço de um usuário")
    @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<EnderecoDTOResponse> atualizaEndereco(
            @RequestBody EnderecoDTORequest dto,
            @RequestParam("id") Long id,
            @RequestHeader(name = "Authorization", required = false) String token) {

        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto, token));
    }

    /**
     * Atualiza um telefone específico com base no ID informado.
     */
    @PutMapping("/telefone")
    @Operation(summary = "Atualizar Telefones", description = "Atualiza os dados de um telefone de um usuário")
    @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Telefone não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<TelefoneDTOResponse> atualizaTelefone(
            @RequestBody TelefoneDTORequest dto,
            @RequestParam("id") Long id,
            @RequestHeader(name = "Authorization", required = false) String token) {

        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto, token));
    }

    /**
     * Remove um usuário do sistema através do e-mail na URL.
     */
    @DeleteMapping("/{email}")
    @Operation(summary = "Deletar Usuários", description = "Deleta um usuário pelo e-mail")
    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email,
                                                      @RequestHeader(name = "Authorization", required = false) String token) {
        // Passa o email e o token para o service satisfazer os 2 argumentos exigidos[cite: 5]
        usuarioService.deleteUsuarioPorEmail(email, token);
        return ResponseEntity.ok().build();
    }
}
