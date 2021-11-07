package br.com.clinipet.ClinipetControl.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE agendamento SET ativo = false WHERE id = ?")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dataInicio;

    private String observacoes;

    private Date dataFim;

    @Builder.Default
    private Boolean ativo = true;

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


    @ToString.Exclude
    @JsonBackReference
    @JsonIgnoreProperties("agendamento")
    @OneToMany(mappedBy = "agendamento", targetEntity = ItemVenda.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<ItemVenda> itensVenda = new ArrayList<>();

}
