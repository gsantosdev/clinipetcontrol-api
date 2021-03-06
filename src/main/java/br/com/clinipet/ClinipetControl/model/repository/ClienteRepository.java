package br.com.clinipet.ClinipetControl.model.repository;

import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.entity.dao.ClienteTipoDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query(value = "SELECT * FROM Cliente WHERE ativo = true and nome LIKE CONCAT('%', :busca, '%') OR cpf LIKE CONCAT('%', :busca, '%') OR telefone LIKE CONCAT('%', :busca, '%')", nativeQuery = true)
    Optional<List<Cliente>> findByNomeOrTelefoneOrCpf(@Param("busca") String busca);


    List<Cliente> findAllByAtivoTrue();

    @Query(value = "SELECT new br.com.clinipet.ClinipetControl.model.entity.dao.ClienteTipoDAO(c.PJ, count(*)) FROM Cliente c WHERE c.ativo = true group by c.PJ")
    List<ClienteTipoDAO> relatorioPfPj();
}
