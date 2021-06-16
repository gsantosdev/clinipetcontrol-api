package br.com.clinipet.ClinipetControl.model.repository;

import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {


}
