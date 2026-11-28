package br.csi.appspring.app_spring_mvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados públicos de um produto do catálogo")
public record ProdutoResponseDTO(
        @Schema(description = "Identificador do produto", example = "1")
        int id,
        @Schema(description = "Marca do piso", example = "Eucafloor")
        String marca,
        @Schema(description = "Modelo do piso", example = "Click")
        String modelo
) {}
