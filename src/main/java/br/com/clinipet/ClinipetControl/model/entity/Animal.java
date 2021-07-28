package br.com.clinipet.ClinipetControl.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

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

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataNascimento;

    private String raca;

    private String especie;

    private String porte;

    private String cor;

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
