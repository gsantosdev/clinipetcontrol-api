package br.com.clinipet.ClinipetControl.model.repository;

import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    //QUERY METHODS
    Optional<Usuario> findByNome(String nome);

    @Query(value = "SELECT * FROM Usuario WHERE nome LIKE CONCAT('%', :busca, '%')", nativeQuery = true)
    Optional<List<Usuario>> findAllByNome(@Param("busca") String busca);

    @Query(value = "SELECT * FROM Usuario WHERE nome = :busca AND id != :id", nativeQuery = true)
    List<Usuario> findEqualNomes(@Param("busca") String busca, @Nullable @Param("id") Long id);

}
