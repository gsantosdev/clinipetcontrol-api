package br.com.clinipet.ClinipetControl.model.repository;


import br.com.clinipet.ClinipetControl.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository  extends JpaRepository<Produto, Long> {


}
