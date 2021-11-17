package br.com.clinipet.ClinipetControl.controller.dto.response;

import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Funcionario;
import br.com.clinipet.ClinipetControl.model.entity.Servico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgendamentoRemarcarResponseDTO {

    private Long id;

    private Date dataHorario;

    private String observacoes;

    private Date dataFim;

    private Long idServico;

    private Long idAnimal;

    private Long idFuncionario;

    private Long idCliente;
}
