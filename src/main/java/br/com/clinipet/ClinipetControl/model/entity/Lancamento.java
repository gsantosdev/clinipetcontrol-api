package br.com.clinipet.ClinipetControl.model.entity;


import br.com.clinipet.ClinipetControl.model.enums.StatusLancamentoEnum;
import br.com.clinipet.ClinipetControl.model.enums.TipoLancamentoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lancamento")
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private BigDecimal valor;

    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(value = EnumType.STRING)
    private TipoLancamentoEnum tipo;

    @Enumerated(value = EnumType.STRING)
    private StatusLancamentoEnum status;

    @ManyToOne
    @JoinColumn(name = "id_venda")
    private Venda venda;

}
