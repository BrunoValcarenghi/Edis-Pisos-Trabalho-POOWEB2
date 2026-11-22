package br.csi.appspring.app_spring_mvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token de acesso e dados básicos do usuário")
public record TokenResponseDTO(
        String token,
        String tipo,
        String nome,
        int usuarioId
) {}
