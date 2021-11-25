package br.com.clinipet.ClinipetControl.model.entity.dao;

import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class LancamentoDAO {

    private Long idLancamento;

    private Long idVenda;

    private BigDecimal valor;

    private String descricao;

    private String cpfCnpj;

    @Enumerated(EnumType.STRING)
    private StatusLancamentoEnum statusLancamento;

    private LocalDateTime updatedAt;

    public LancamentoDAO(Long id, Long idVenda, String cpfCnpj, String descricao, BigDecimal valor, StatusLancamentoEnum statusLancamento, LocalDateTime updatedAt) {
        this.idLancamento = id;
        this.idVenda = idVenda;
        this.descricao = descricao;
        this.valor = valor;
        this.statusLancamento = statusLancamento;
        this.updatedAt = updatedAt;
        this.cpfCnpj = cpfCnpj;
    }



    public LancamentoDAO(Long id, String descricao, BigDecimal valor, StatusLancamentoEnum statusLancamento, LocalDateTime updatedAt) {
        this.idLancamento = id;
        this.descricao = descricao;
        this.valor = valor;
        this.statusLancamento = statusLancamento;
        this.updatedAt = updatedAt;
    }
}
