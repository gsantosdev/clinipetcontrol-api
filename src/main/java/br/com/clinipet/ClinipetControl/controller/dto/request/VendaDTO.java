package br.com.clinipet.ClinipetControl.controller.dto.request;


import br.com.clinipet.ClinipetControl.model.enums.StatusVendaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendaDTO {

    private List<ItemVendaDTO> itensVendaDTO;

    private StatusVendaEnum status;

}
