package br.com.clinipet.ClinipetControl.model.entity.dao;

import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
public class LancamentoDAO {

    private Long idLancamento;

    private BigDecimal valor;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusLancamentoEnum statusLancamento;

    private LocalDateTime updatedAt;

    public LancamentoDAO(Long id, String descricao, BigDecimal valor, StatusLancamentoEnum statusLancamento, LocalDateTime updatedAt) {
        this.idLancamento = id;
        this.descricao = descricao;
        this.valor = valor;
        this.statusLancamento = statusLancamento;
        this.updatedAt = updatedAt;
    }
}
