package br.csi.appspring.app_spring_mvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para login")
public record UsuarioLoginRequestDTO(
        @NotBlank
        @Email
        @Schema(description = "Email cadastrado", example = "bruno@email.com")
        String email,

        @NotBlank
        @Schema(description = "Senha da conta")
        String senha
) {}
