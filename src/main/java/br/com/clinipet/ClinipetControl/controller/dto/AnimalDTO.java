package br.com.clinipet.ClinipetControl.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {

    private Long id;

    private String nome;

    private String sexo;

    private String idade;

    private String raca;

    private String tipo;

    private String cor;

    private String peso;

    @Nullable
    private String alergias;

    @Nullable
    private String patologias;

    @Nullable
    private String medicamentos;

    private Long idCliente;

}