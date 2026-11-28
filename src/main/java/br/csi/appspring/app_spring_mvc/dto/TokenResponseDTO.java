package br.csi.appspring.app_spring_mvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token de acesso e dados básicos do usuário")
public record TokenResponseDTO(
        @Schema(description = "JWT para usar no cabeçalho Authorization", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(description = "Tipo de autenticação", example = "Bearer")
        String tipo,
        @Schema(description = "Nome público do usuário", example = "Bruno Machado")
        String nome,
        @Schema(description = "Identificador do usuário autenticado", example = "2")
        int usuarioId
) {}
