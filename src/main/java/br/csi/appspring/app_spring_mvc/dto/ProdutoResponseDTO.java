package br.csi.appspring.app_spring_mvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados públicos de um produto do catálogo")
public record ProdutoResponseDTO(
        int id,
        String marca,
        String modelo
) {}
