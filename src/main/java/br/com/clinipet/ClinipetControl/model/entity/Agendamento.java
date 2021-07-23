package br.com.clinipet.ClinipetControl.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dataInicio;

    private String observacoes;

    private Date dataFim;


    @JsonBackReference
    @JsonIgnoreProperties("servico")
    @ManyToOne
    @JoinColumn(name = "idServico")
    private Servico servico;

    @JsonBackReference
    @JsonIgnoreProperties("animal")
    @ManyToOne
    @JoinColumn(name = "idAnimal")
    private Animal animal;

    @JsonBackReference
    @JsonIgnoreProperties("funcionario")
    @ManyToOne
    @JoinColumn(name = "idFuncionario")
    private Funcionario funcionario;

}
