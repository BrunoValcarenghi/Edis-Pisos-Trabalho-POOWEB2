package br.csi.appspring.app_spring_mvc.service;

import br.csi.appspring.app_spring_mvc.dto.AvaliacaoRequestDTO;
import br.csi.appspring.app_spring_mvc.dto.AvaliacaoResponseDTO;
import br.csi.appspring.app_spring_mvc.model.Avaliacao;
import br.csi.appspring.app_spring_mvc.repository.AvaliacaoRepository;
import br.csi.appspring.app_spring_mvc.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                           UsuarioRepository usuarioRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarTodas() {
        return avaliacaoRepository.findAllByOrderByIdDesc().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public AvaliacaoResponseDTO criar(AvaliacaoRequestDTO request, int usuarioId) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setTexto(request.texto().trim());
        avaliacao.setAutor(usuarioRepository.getReferenceById(usuarioId));

        return converterParaDTO(avaliacaoRepository.save(avaliacao));
    }

    @Transactional
    public AvaliacaoResponseDTO editar(int avaliacaoId, AvaliacaoRequestDTO request, int usuarioId) {
        Avaliacao avaliacao = buscarPropria(avaliacaoId, usuarioId);
        avaliacao.setTexto(request.texto().trim());

        return converterParaDTO(avaliacaoRepository.save(avaliacao));
    }

    @Transactional
    public void excluir(int avaliacaoId, int usuarioId) {
        Avaliacao avaliacao = buscarPropria(avaliacaoId, usuarioId);
        avaliacaoRepository.delete(avaliacao);
    }

    private Avaliacao buscarPropria(int avaliacaoId, int usuarioId) {
        return avaliacaoRepository.findByIdAndAutor_Id(avaliacaoId, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Avaliação não encontrada."));
    }

    private AvaliacaoResponseDTO converterParaDTO(Avaliacao avaliacao) {
        return new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getTexto(),
                avaliacao.getAutor().getNome(),
                avaliacao.getAutor().getId());
    }
}
