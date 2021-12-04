package br.com.clinipet.ClinipetControl.model.entity.dao;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ContagemServicoDAO {

    public ContagemServicoDAO(String nomeServico, Long contagem) {
        this.label = nomeServico;
        this.value = contagem;
    }

    private String label;

    private Long value;


}
