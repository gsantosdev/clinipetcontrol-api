package br.com.clinipet.ClinipetControl.model.repository;


import br.com.clinipet.ClinipetControl.model.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @Query(value = "SELECT * FROM Funcionario WHERE ativo = true and nome LIKE CONCAT('%', :busca, '%') OR telefone LIKE CONCAT('%', :busca, '%')", nativeQuery = true)
    Optional<List<Funcionario>> findByNomeOrTelefone(@Param("busca") String busca);
}
