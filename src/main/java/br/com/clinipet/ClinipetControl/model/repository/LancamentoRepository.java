package br.com.clinipet.ClinipetControl.model.repository;

import br.com.clinipet.ClinipetControl.model.entity.Lancamento;
import br.com.clinipet.ClinipetControl.model.entity.dao.LancamentoDAO;
import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import br.com.clinipet.ClinipetControl.model.enums.TipoLancamentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    @Query(value = "select sum(l.valor) from Lancamento l " +
            "where l.tipo =:tipo and l.status = :status")
    BigDecimal obterSaldoPorTipoLancamentoStatus(@Param("tipo") TipoLancamentoEnum tipo, @Param("status") StatusLancamentoEnum status);


    @Query(value = "SELECT new " +
            "br.com.clinipet.ClinipetControl.model.entity.dao.LancamentoDAO( l.id, v.id, l.descricao, l.valor," +
            " l.status, l.updatedAt)" +
            " FROM Lancamento l LEFT JOIN l.venda v WHERE " +
            "l.status ='AGUARDANDO_PAGAMENTO' and l.tipo='RECEITA' order by l.updatedAt desc ")
    List<LancamentoDAO> findLancamentosReceitaOrderedByDatUpdate();

    @Query(value = "SELECT new " +
            "br.com.clinipet.ClinipetControl.model.entity.dao.LancamentoDAO( l.id, l.descricao, l.valor," +
            " l.status, l.updatedAt)" +
            " FROM Lancamento l WHERE " +
            "l.status ='AGUARDANDO_PAGAMENTO' and l.tipo='DESPESA' order by l.updatedAt desc ")
    List<LancamentoDAO> findLancamentosDespesaOrderedByDatUpdate();


}
