package br.com.clinipet.ClinipetControl.model.entity;

import br.com.clinipet.ClinipetControl.model.enums.StatusVendaEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;
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
    private Date dataCriacao;

    private double valorTotal;


    @JsonBackReference
    @OneToMany(mappedBy = "venda", targetEntity = ItemVenda.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<ItemVenda> itensVenda;


    @JsonIgnoreProperties("vendas")
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    private String tipo;

    @PreUpdate
    @PrePersist
    public void calcValorTotal() {

        itensVenda.forEach(item -> valorTotal += item.getAgendamento().getServico().getValorItem() * item.getQuantidade());
    }
}
