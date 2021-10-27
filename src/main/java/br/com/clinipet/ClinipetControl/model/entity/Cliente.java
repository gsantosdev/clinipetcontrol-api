package br.com.clinipet.ClinipetControl.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
//@SQLDelete(sql = "UPDATE cliente SET ativo = false WHERE id = ?")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataNascimento;

    private String telefone;

    private String email;

    private String logradouro;

    private Long numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String uf;

    //private Boolean ativo;

    @ToString.Exclude
    @JsonBackReference
    @JsonIgnoreProperties("cliente")
    @OneToMany(mappedBy = "cliente", targetEntity = Animal.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Animal> animais = new ArrayList<>();


}
