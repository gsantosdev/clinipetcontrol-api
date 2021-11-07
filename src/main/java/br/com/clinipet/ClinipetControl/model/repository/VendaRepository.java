package br.com.clinipet.ClinipetControl.model.repository;

import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.entity.Venda;
import br.com.clinipet.ClinipetControl.model.entity.dao.ordemDeServicoDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findAllByCliente(Cliente cliente);

    @Query(value = "SELECT new " +
            "br.com.clinipet.ClinipetControl.model.entity.dao.ordemDeServicoDAO( l.id, l.descricao, l.valor," +
            " l.status, l.dataExecucao ,c.nome, c.cpf)" +
            " FROM Lancamento l JOIN l.venda v JOIN v.cliente c WHERE c.nome LIKE CONCAT('%', ?1, '%') " +
            "OR c.cpf LIKE CONCAT('%', ?1, '%') AND v.tipo ='servico' AND l.status <> 'AGUARDANDO_PAGAMENTO' AND l.status <> 'CONCLUIDO' AND l.status <> 'CANCELADO' order by l.dataExecucao desc ")
    List<ordemDeServicoDAO> findOrdensByCliente(@Param("busca") String busca);
}
