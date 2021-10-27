package br.com.clinipet.ClinipetControl.controller.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemVendaDTO {

    @Nullable
    private AgendamentoRequestDTO agendamento;

    @Nullable
    private Long idProduto;

    private Integer quantidade;
}
