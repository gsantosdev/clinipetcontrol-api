package br.com.clinipet.ClinipetControl.controller.dto.response;

import br.com.clinipet.ClinipetControl.model.entity.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoVendidoResponse {

    public String label;

    public Long value;

    public Double valorTotal;
}
