package br.com.clinipet.ClinipetControl.controller.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FechamentoCaixaResponseDTO {

    @Builder.Default
    public BigDecimal produtos = BigDecimal.ZERO;

    @Builder.Default
    public BigDecimal servicos = BigDecimal.ZERO;

    @Builder.Default
    public BigDecimal outrasReceitas = BigDecimal.ZERO;

    @Builder.Default
    public BigDecimal depositos = BigDecimal.ZERO;

    @Builder.Default
    public BigDecimal sangrias = BigDecimal.ZERO;

    @Builder.Default
    public BigDecimal despesas = BigDecimal.ZERO;

    @Builder.Default
    public BigDecimal subtotalEntrada = BigDecimal.ZERO;

    @Builder.Default
    public BigDecimal subtotalSaida = BigDecimal.ZERO;

    @Builder.Default
    public BigDecimal totalVendas = BigDecimal.ZERO;

    private Date inicio;

    private Date fim;
}
