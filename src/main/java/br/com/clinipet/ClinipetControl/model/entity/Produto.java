package br.com.clinipet.ClinipetControl.model.entity;


import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.enums.StatusEstoqueEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String marca;

    @Column(precision=10, scale=2)
    private double valorBase;

    @Column(precision=10, scale=2)
    private double margemLucro;

    @Column(precision=10, scale=2)
    private double valorItem;

    private Long quantidadeEstoque;

    private Long estoqueMinimo;

    private Long estoqueMaximo;


    @PreUpdate
    @PrePersist
    public void calcValorTotal() {
        valorItem = valorBase * (margemLucro / 100);
    }

    public void baixaEstoque(Long quantidade) {

        if (quantidadeEstoque == 0) {
            throw new RegraNegocioException("Estoque do produto já esgotado!");
        }
        if (quantidadeEstoque - quantidade < 0){
            throw new RegraNegocioException("A quantidade em estoque não pode ser menor do que 0!");
        }
        quantidadeEstoque -= quantidade;
    }

    public void entradaEstoque(Long quantidade) {
        quantidadeEstoque += quantidade;
    }

    public StatusEstoqueEnum statusEstoque() {

        if (quantidadeEstoque > estoqueMaximo) {
            return StatusEstoqueEnum.MAXIMO;
        }

        if (quantidadeEstoque < estoqueMinimo) {
            return StatusEstoqueEnum.MINIMO;
        }

        return StatusEstoqueEnum.OK;
    }


}
