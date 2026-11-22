package br.csi.appspring.app_spring_mvc.service;

import br.csi.appspring.app_spring_mvc.dto.ProdutoResponseDTO;
import br.csi.appspring.app_spring_mvc.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(produto -> new ProdutoResponseDTO(
                        produto.getId(), produto.getMarca(), produto.getModelo()))
                .toList();
    }
}
