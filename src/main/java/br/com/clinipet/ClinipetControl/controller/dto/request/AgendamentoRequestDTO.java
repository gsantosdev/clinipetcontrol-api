package br.com.clinipet.ClinipetControl.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoRequestDTO {

    private Date dataHorario;

    private String observacoes;

    private Long duracaoAprox;

    private Long idServico;

    private Long idAnimal;

    private Long idFuncionario;
}
