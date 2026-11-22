package br.csi.appspring.app_spring_mvc.controller;

import br.csi.appspring.app_spring_mvc.dto.TokenResponseDTO;
import br.csi.appspring.app_spring_mvc.dto.UsuarioCadastroRequestDTO;
import br.csi.appspring.app_spring_mvc.dto.UsuarioLoginRequestDTO;
import br.csi.appspring.app_spring_mvc.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação")
public class AutenticacaoController {

    private final UsuarioService usuarioService;

    public AutenticacaoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma conta e emite um JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada"),
            @ApiResponse(responseCode = "400", description = "Campos inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    public TokenResponseDTO cadastrar(@Valid @RequestBody UsuarioCadastroRequestDTO request) {
        return usuarioService.cadastrar(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica e emite um JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação concluída"),
            @ApiResponse(responseCode = "401", description = "Email ou senha inválidos")
    })
    public TokenResponseDTO entrar(@Valid @RequestBody UsuarioLoginRequestDTO request) {
        return usuarioService.entrar(request);
    }
}
