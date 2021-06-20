package br.com.clinipet.ClinipetControl.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @JsonIgnoreProperties("animal")
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

}
