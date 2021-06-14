package br.com.clinipet.ClinipetControl.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinColumn(name="id_cliente")
    private Cliente cliente;

    @ManyToMany
    @JoinColumn(name="id_animal")
    private Animal animal;

    @ManyToMany
    @JoinColumn(name="id_servico")
    private Servico servico;

    @ManyToMany
    @JoinColumn(name="id_funcionario")
    private Funcionario funcionario;

    @Column(name = "data_agendada")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime dataAgendada;

    private Time tempoEstimado;

    private boolean realizado;

}
