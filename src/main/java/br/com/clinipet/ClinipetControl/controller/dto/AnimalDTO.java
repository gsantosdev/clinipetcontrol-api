package br.com.clinipet.ClinipetControl.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {

    private Long id;

    private String nome;

    private Integer ano;

    private Integer mes;

    private String cor;

    private String tipo;

    private Long idCliente;

}