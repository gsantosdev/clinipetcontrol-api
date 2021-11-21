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

    public Produto produto;

    public Long quantidade;

    public Double valorTotal;
}
