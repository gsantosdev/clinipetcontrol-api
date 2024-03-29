package br.com.clinipet.ClinipetControl.controller.mapper;


import br.com.clinipet.ClinipetControl.controller.dto.request.AgendamentoRequestDTO;
import br.com.clinipet.ClinipetControl.controller.dto.response.AgendamentoRemarcarResponseDTO;
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

import java.util.Date;

import static org.mapstruct.ReportingPolicy.IGNORE;


@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
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
    @Mapping(source = "dataHorario", target = "dataInicio")
    @Mapping(source = ".", target = "dataFim", qualifiedByName = "getDataFim")
    public abstract Agendamento toEntity(AgendamentoRequestDTO agendamentoRequestDTO);


    @Mapping(source = "servico.id", target = "idServico")
    @Mapping(source = "animal.id", target = "idAnimal")
    @Mapping(source = "funcionario.id", target = "idFuncionario")
    @Mapping(source = "animal.cliente.id", target = "idCliente")
    @Mapping(source = "dataInicio", target = "dataHorario")
    public abstract AgendamentoRemarcarResponseDTO toDTO(Agendamento agendamento);


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

    @Named("getDataFim")
    Date getDataFim(AgendamentoRequestDTO agendamentoRequestDTO) {

        Servico servico = servicoService.obterPorId(agendamentoRequestDTO.getIdServico())
                .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado!"));

        return Date.from(agendamentoRequestDTO.getDataHorario().toInstant()
                .plusSeconds(servico.getDuracaoEstimada() * 60));
    }




}
