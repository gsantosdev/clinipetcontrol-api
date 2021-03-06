package br.com.clinipet.ClinipetControl.model.entity;

import br.com.clinipet.ClinipetControl.model.enums.TipoUsuarioEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
@SQLDelete(sql = "UPDATE usuario SET ativo = false WHERE id = ?")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @JsonIgnore
    private String senha;

    @Enumerated(value = EnumType.STRING)
    private TipoUsuarioEnum tipo;

    private Boolean isCaixaOpen;

    @Builder.Default
    private Boolean ativo = true;


}