package br.com.clinipet.ClinipetControl.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(precision = 10, scale = 2)
    private double valorTotal;


    @JsonBackReference
    @OneToMany(mappedBy = "venda", targetEntity = ItemVenda.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<ItemVenda> itensVenda;


    @JsonIgnoreProperties("vendas")
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    private String cpfCnpj;

    private String tipo;

    @OneToMany(mappedBy = "venda")
    @JsonBackReference
    private List<Lancamento> lancamento;

    @PreUpdate
    @PrePersist
    public void calcValorTotal() {
        itensVenda.forEach(item -> {

            if (item.getAgendamento() != null) {
                valorTotal += item.getAgendamento().getServico().getValorItem() * item.getQuantidade();
            } else {
                valorTotal += item.getProduto().getValorItem() * item.getQuantidade();
            }

        });
    }
}
