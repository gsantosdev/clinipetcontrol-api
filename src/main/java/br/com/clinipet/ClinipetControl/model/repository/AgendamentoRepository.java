package br.com.clinipet.ClinipetControl.model.repository;

import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query(value = "select * from agendamento where (:inicio between data_inicio and DATE_ADD(data_fim, INTERVAL -1 MINUTE) or " +
            ":fim between data_inicio and data_fim) and (id_funcionario = :idFuncionario or id_animal = :idAnimal)", nativeQuery = true)
    List<Agendamento> findExistentAgendamentosByRange(@Param("inicio") Date inicio,
                                                      @Param("fim") Date fim,
                                                      @Param("idFuncionario") Long idFuncionario,
                                                      @Param("idAnimal") Long idAnimal);

}
