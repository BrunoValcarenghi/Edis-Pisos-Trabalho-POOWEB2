package br.csi.appspring.app_spring_mvc.controller;

import br.csi.appspring.app_spring_mvc.dto.AvaliacaoRequestDTO;
import br.csi.appspring.app_spring_mvc.dto.AvaliacaoResponseDTO;
import br.csi.appspring.app_spring_mvc.infra.security.UsuarioPrincipal;
import br.csi.appspring.app_spring_mvc.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@Tag(name = "Avaliações")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @GetMapping
    @Operation(summary = "Lista as avaliações públicas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliações retornadas com sucesso")
    })
    public ResponseEntity<List<AvaliacaoResponseDTO>> listar() {
        return ResponseEntity.ok(avaliacaoService.listarTodas());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Publica uma avaliação como o usuário autenticado",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Avaliação criada"),
            @ApiResponse(responseCode = "400", description = "Texto inválido"),
            @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
    })
    public AvaliacaoResponseDTO criar(
            @Valid @RequestBody AvaliacaoRequestDTO request,
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return avaliacaoService.criar(request, usuario.getId());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edita uma avaliação própria",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada"),
            @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada ou pertence a outra pessoa")
    })
    public ResponseEntity<AvaliacaoResponseDTO> editar(
            @Parameter(description = "Identificador da avaliação", example = "1")
            @PathVariable int id,
            @Valid @RequestBody AvaliacaoRequestDTO request,
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return ResponseEntity.ok(avaliacaoService.editar(id, request, usuario.getId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui uma avaliação própria",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Avaliação excluída"),
            @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada ou pertence a outra pessoa")
    })
    public void excluir(
            @Parameter(description = "Identificador da avaliação", example = "1")
            @PathVariable int id,
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        avaliacaoService.excluir(id, usuario.getId());
    }
}
