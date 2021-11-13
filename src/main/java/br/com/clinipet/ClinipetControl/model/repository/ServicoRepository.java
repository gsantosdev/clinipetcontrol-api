package br.com.clinipet.ClinipetControl.model.repository;

import br.com.clinipet.ClinipetControl.model.entity.Servico;
import br.com.clinipet.ClinipetControl.model.entity.dao.ServicoDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {


    @Query("SELECT new br.com.clinipet.ClinipetControl.model.entity.dao.ServicoDAO(s.nome, l.status, l.updatedAt, f.nome) " +
            "FROM Agendamento a " +
            "JOIN a.itensVenda iv " +
            "JOIN iv.venda v " +
            "JOIN v.lancamento l " +
            "JOIN a.funcionario f " +
            "JOIN a.servico s " +
            "WHERE a.animal.id = :id and l.status = 'CONCLUIDO' order by l.updatedAt desc")
    List<ServicoDAO> getHistorico(Long id);

}
