package br.com.clinipet.ClinipetControl.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgendamentoResponseDTO {

    private Long id;

    private String title;

    private Date start;

    private Date end;

    private String nomeFuncionario;

    private String nomeAnimal;

    private String nomeServico;

    private String nomeProprietario;

    private String telefoneProprietario;

}
