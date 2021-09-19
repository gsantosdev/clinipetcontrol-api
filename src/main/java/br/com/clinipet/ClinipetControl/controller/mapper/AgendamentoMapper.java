package br.com.clinipet.ClinipetControl.controller.mapper;


import br.com.clinipet.ClinipetControl.controller.dto.request.AgendamentoRequestDTO;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Agendamento;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Funcionario;
import br.com.clinipet.ClinipetControl.model.entity.Servico;
import br.com.clinipet.ClinipetControl.service.AnimalService;
import br.com.clinipet.ClinipetControl.service.FuncionarioService;
import br.com.clinipet.ClinipetControl.service.ServicoService;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper
public abstract class AgendamentoMapper {

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Mapping(source = "idServico", target = "servico", qualifiedByName = "getServico")
    @Mapping(source = "idAnimal", target = "animal", qualifiedByName = "getAnimal")
    @Mapping(source = "idFuncionario", target = "funcionario", qualifiedByName = "getFuncionario")
    public abstract Agendamento toEntity(AgendamentoRequestDTO agendamentoRequestDTO);


    @Named("getServico")
    Servico getServico(@NonNull final Long id) {
        return servicoService.obterPorId(id).orElseThrow(() -> new RegraNegocioException("Serviço não encontrado!"));
    }

    @Named("getAnimal")
    Animal getAnimal(@NonNull final Long id) {
        return animalService.obterPorId(id).orElseThrow(() -> new RegraNegocioException("Animal não encontrado"));
    }

    @Named("getFuncionario")
    Funcionario getFuncionario(@NonNull final Long id) {
        return funcionarioService.obterPorId(id).orElseThrow(() -> new RegraNegocioException("Funcionário não encontrado"));
    }


}
