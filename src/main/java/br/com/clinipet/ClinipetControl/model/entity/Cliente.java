package br.com.clinipet.ClinipetControl.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cpf;

    private String rg;

    @Column(name = "data_nascimento")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private Date dataNascimento;

    private String telefone;

    private String email;

    private String logradouro;

    private Long numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String uf;


}
