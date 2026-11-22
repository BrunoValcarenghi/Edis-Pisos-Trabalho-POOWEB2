package br.csi.appspring.app_spring_mvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de uma avaliação publicada")
public record AvaliacaoResponseDTO(
        int id,
        String texto,
        String nomeAutor,
        int idAutor
) {}
