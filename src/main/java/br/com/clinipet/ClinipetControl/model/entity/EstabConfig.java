package br.com.clinipet.ClinipetControl.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "EstabConfig")

public class EstabConfig {

    private String horarioInicio;

    private String horarioFim;
}
