package br.csi.appspring.app_spring_mvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de usuário")
public record UsuarioCadastroRequestDTO(
        @NotBlank
        @Size(max = 120)
        @Schema(description = "Nome do usuário", example = "Bruno Machado")
        String nome,

        @NotBlank
        @Email
        @Size(max = 180)
        @Schema(description = "Email usado para autenticação", example = "bruno@email.com")
        String email,

        @NotBlank
        @Size(min = 8, max = 72)
        @Schema(description = "Senha da conta", example = "SenhaSegura123")
        String senha
) {}
