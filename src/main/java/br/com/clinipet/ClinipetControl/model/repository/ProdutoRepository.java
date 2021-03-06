package br.com.clinipet.ClinipetControl.model.repository;


import br.com.clinipet.ClinipetControl.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(value = "SELECT * FROM Produto WHERE ativo = true and nome LIKE CONCAT('%', :busca, '%') or marca LIKE CONCAT('%', :busca, '%') and ativo = true", nativeQuery = true)
    List<Produto> findByNomeOrMarca(@Param("busca") String busca);

    @Query(value = "SELECT * FROM Produto WHERE (nome LIKE CONCAT('%', :busca, '%') or marca LIKE CONCAT('%', :busca, '%')) and quantidade_estoque > 0 and ativo = true", nativeQuery = true)
    List<Produto> findByNomeOrMarcaComEstoque(@Param("busca") String busca);

    List<Produto> findAllByAtivoTrue();

}
