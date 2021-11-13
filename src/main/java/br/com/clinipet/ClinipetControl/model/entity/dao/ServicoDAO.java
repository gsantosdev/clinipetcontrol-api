package br.com.clinipet.ClinipetControl.model.entity.dao;

import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
public class ServicoDAO {

    private String nomeServico;

    @Enumerated(EnumType.STRING)
    private StatusLancamentoEnum status;

    private LocalDateTime updatedAt;

    private String nomeFuncionario;

    public ServicoDAO(String nomeServico, StatusLancamentoEnum status, LocalDateTime updatedAt, String nomeFuncionario) {
        this.nomeServico = nomeServico;
        this.status = status;
        this.updatedAt = updatedAt;
        this.nomeFuncionario = nomeFuncionario;
    }


}
