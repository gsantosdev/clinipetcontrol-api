package br.com.clinipet.ClinipetControl.model.repository;

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
    Optional<Usuario> findByNomeAndAtivoTrue(String nome);


    @Query(value = "FROM Usuario u WHERE u.nome like %:busca% and u.ativo = true")
    List<Usuario> findAllByNomeAndAtivoTrue(@Param("busca") String busca);


    @Query(value = "SELECT * FROM Usuario WHERE nome = :busca and u.ativo = true AND id <> :id", nativeQuery = true)
    List<Usuario> findEqualNomesAndAtivoTrue(@Param("busca") String busca, @Nullable @Param("id") Long id);

}
