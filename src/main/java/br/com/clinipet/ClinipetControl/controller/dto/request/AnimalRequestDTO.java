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
public class AnimalRequestDTO {

    private Long id;

    private String nome;

    private String sexo;

    private String idade;

    private String raca;

    private String especie;

    private String cor;

    private String porte;

    @Nullable
    private String alergias;

    @Nullable
    private String patologias;

    @Nullable
    private String medicamentos;

    private Long idCliente;

}