package br.csi.appspring.app_spring_mvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de uma avaliação publicada")
public record AvaliacaoResponseDTO(
        @Schema(description = "Identificador da avaliação", example = "1")
        int id,
        @Schema(description = "Texto publicado", example = "Gostei muito do piso.")
        String texto,
        @Schema(description = "Nome público do autor", example = "Bruno Machado")
        String nomeAutor,
        @Schema(description = "Identificador do autor", example = "2")
        int idAutor
) {}
