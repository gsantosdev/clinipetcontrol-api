package br.com.clinipet.ClinipetControl.model.entity;


import br.com.clinipet.ClinipetControl.model.enums.TipoFuncionario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "funcionário")

public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;

    private TipoFuncionario tipo;

    @Column(name = "data_admissao")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private Date dataAdmissao;

}
