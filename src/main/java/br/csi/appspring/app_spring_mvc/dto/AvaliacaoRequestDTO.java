package br.csi.appspring.app_spring_mvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para publicação ou edição de avaliação")
public record AvaliacaoRequestDTO(
        @NotBlank
        @Size(max = 2000)
        @Schema(description = "Texto da avaliação", example = "Gostei muito do piso.")
        String texto
) {}
