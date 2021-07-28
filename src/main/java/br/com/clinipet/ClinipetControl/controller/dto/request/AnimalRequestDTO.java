package br.com.clinipet.ClinipetControl.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRequestDTO {

    private Long id;

    private String nome;

    private String sexo;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataNascimento;

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