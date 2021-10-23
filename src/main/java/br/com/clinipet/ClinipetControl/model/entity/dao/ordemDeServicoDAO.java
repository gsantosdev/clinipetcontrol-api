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
public class ordemDeServicoDAO {

    private Long idLancamento;

    private BigDecimal valor;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusLancamentoEnum statusOrdem;

    private LocalDateTime dataCriacao;

    private String nome;

    private String cpf;


    public ordemDeServicoDAO(Long id, String descricao, BigDecimal valor, StatusLancamentoEnum statusLancamentoEnum, LocalDateTime dataCriacao, String nome, String cpf) {
        this.idLancamento = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataCriacao = dataCriacao;
        this.statusOrdem = statusLancamentoEnum;
        this.nome = nome;
        this.cpf = cpf;
    }
}
