package br.com.clinipet.ClinipetControl.model.entity;

import br.com.clinipet.ClinipetControl.model.enums.TipoAnimal;
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

public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(name = "data_nascimento")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class) //Converter para tipo de data que o banco suporte
    private Date dataNascimento;

    private String cor;

    @Enumerated(value = EnumType.STRING)
    private TipoAnimal tipo;

    @ManyToOne
    @JoinColumn(name="id_cliente")
    private Cliente cliente;

}
