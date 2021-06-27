package br.com.clinipet.ClinipetControl.model.repository;

import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

}
