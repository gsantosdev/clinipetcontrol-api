package br.com.clinipet.ClinipetControl.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoDTO {

    private Date dataHorario;

    private String observacoes;

    private Long duracaoAprox;

    private Long idServico;

    private Long idAnimal;

    private Long idFuncionario;
}
