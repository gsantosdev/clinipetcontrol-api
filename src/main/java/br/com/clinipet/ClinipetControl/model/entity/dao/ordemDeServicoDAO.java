package br.com.clinipet.ClinipetControl.model.entity.dao;

import br.com.clinipet.ClinipetControl.controller.dto.response.AgendamentoRemarcarResponseDTO;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
public class ordemDeServicoDAO {

    private Long idLancamento;

    private BigDecimal valor;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusLancamentoEnum statusOrdem;

    private Date dataExecucao;

    private String nome;

    private String cpf;

    public ordemDeServicoDAO(Long id, String descricao, BigDecimal valor, StatusLancamentoEnum statusLancamentoEnum, Date dataExecucao, String nome, String cpf) {
        this.idLancamento = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataExecucao = DateUtils.addHours(dataExecucao, 3);
        this.statusOrdem = statusLancamentoEnum;
        this.nome = nome;
        this.cpf = cpf;
    }

    private AgendamentoRemarcarResponseDTO agendamento;
}
