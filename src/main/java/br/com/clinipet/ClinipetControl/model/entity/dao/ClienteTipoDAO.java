package br.com.clinipet.ClinipetControl.model.entity.dao;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ClienteTipoDAO {

    public ClienteTipoDAO(Boolean PJ, Long contagem) {
        this.label = PJ ? "PJ" : "PF";
        this.value = contagem;
    }

    private String label;

    private Long value;


}
