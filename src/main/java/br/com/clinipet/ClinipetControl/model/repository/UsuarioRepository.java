package br.com.clinipet.ClinipetControl.model.repository;

import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    //QUERY METHODS
    Optional<Usuario> findByNome(String nome);

}
