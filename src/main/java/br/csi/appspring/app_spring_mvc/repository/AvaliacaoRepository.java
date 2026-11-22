package br.csi.appspring.app_spring_mvc.repository;

import br.csi.appspring.app_spring_mvc.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {
    List<Avaliacao> findAllByOrderByIdDesc();
    Optional<Avaliacao> findByIdAndAutor_Id(int id, int autorId);
}
