package br.com.clinipet.ClinipetControl.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "servico")
@SQLDelete(sql = "UPDATE servico SET ativo = false WHERE id = ?")

public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String observacoes;

    private Long duracaoEstimada;

    @Column(precision = 10, scale = 2)
    private Double valorBase;

    @Column(precision = 10, scale = 2)
    private Double margemLucro;

    @Column(precision = 10, scale = 2)
    private Double valorItem;


    @PreUpdate
    @PrePersist
    public void calcValorTotal() {
        valorItem = valorBase * ((margemLucro / 100) + 1 );

    }

    @Builder.Default
    private Boolean ativo = true;


}

