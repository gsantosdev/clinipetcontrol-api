package br.com.clinipet.ClinipetControl.controller.dto.request;


import br.com.clinipet.ClinipetControl.model.enums.TipoUsuarioEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    private String nome;

    private String senha;

    private TipoUsuarioEnum tipo;
}
