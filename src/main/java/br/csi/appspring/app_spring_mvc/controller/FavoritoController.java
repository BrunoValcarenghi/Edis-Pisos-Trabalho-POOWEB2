package br.csi.appspring.app_spring_mvc.controller;

import br.csi.appspring.app_spring_mvc.dto.ProdutoResponseDTO;
import br.csi.appspring.app_spring_mvc.infra.security.UsuarioPrincipal;
import br.csi.appspring.app_spring_mvc.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@Tag(name = "Favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @GetMapping
    @Operation(summary = "Lista os favoritos do usuário autenticado",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de favoritos"),
            @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> listar(
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return ResponseEntity.ok(favoritoService.listar(usuario.getId()));
    }

    @PutMapping("/{produtoId}")
    @Operation(summary = "Adiciona um produto aos favoritos",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Favorito adicionado"),
            @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> adicionar(
            @PathVariable int produtoId,
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        favoritoService.adicionar(usuario.getId(), produtoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{produtoId}")
    @Operation(summary = "Remove um produto dos favoritos",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Favorito removido"),
            @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
    })
    public ResponseEntity<Void> remover(
            @PathVariable int produtoId,
            @AuthenticationPrincipal UsuarioPrincipal usuario) {
        favoritoService.remover(usuario.getId(), produtoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
