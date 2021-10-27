package br.com.clinipet.ClinipetControl.model.repository;


import br.com.clinipet.ClinipetControl.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(value = "SELECT * FROM Produto WHERE nome LIKE CONCAT('%', :busca, '%') or marca LIKE CONCAT('%', :busca, '%')", nativeQuery = true)
    List<Produto> findByNomeOrMarca(@Param("busca") String busca);

}
