package br.com.clinipet.ClinipetControl.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoIdsDTO {

    private List<Long> idsLancamento;

    private Date dataInicio;
}
