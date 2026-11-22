package br.csi.appspring.app_spring_mvc.service;

import br.csi.appspring.app_spring_mvc.dto.ProdutoResponseDTO;
import br.csi.appspring.app_spring_mvc.model.Produto;
import br.csi.appspring.app_spring_mvc.model.Usuario;
import br.csi.appspring.app_spring_mvc.repository.ProdutoRepository;
import br.csi.appspring.app_spring_mvc.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FavoritoService {

    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;

    public FavoritoService(UsuarioRepository usuarioRepository,
                           ProdutoRepository produtoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listar(int usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        return usuario.getProdutosFavoritos().stream()
                .map(produto -> new ProdutoResponseDTO(
                        produto.getId(), produto.getMarca(), produto.getModelo()))
                .toList();
    }

    @Transactional
    public void adicionar(int usuarioId, int produtoId) {
        Usuario usuario = buscarUsuario(usuarioId);
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado."));

        boolean jaFavoritado = usuario.getProdutosFavoritos().stream()
                .anyMatch(item -> item.getId() == produtoId);

        if (!jaFavoritado) {
            usuario.getProdutosFavoritos().add(produto);
        }
    }

    @Transactional
    public void remover(int usuarioId, int produtoId) {
        Usuario usuario = buscarUsuario(usuarioId);
        usuario.getProdutosFavoritos().removeIf(item -> item.getId() == produtoId);
    }

    private Usuario buscarUsuario(int usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }
}
