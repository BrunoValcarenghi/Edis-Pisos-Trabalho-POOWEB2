package br.csi.appspring.app_spring_mvc.repository;

import br.csi.appspring.app_spring_mvc.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {}
