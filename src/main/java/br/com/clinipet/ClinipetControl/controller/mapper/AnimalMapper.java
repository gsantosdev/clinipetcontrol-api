package br.com.clinipet.ClinipetControl.controller.mapper;

import br.com.clinipet.ClinipetControl.controller.dto.request.AnimalRequestDTO;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Animal;
import br.com.clinipet.ClinipetControl.model.entity.Cliente;
import br.com.clinipet.ClinipetControl.service.ClienteService;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public abstract class AnimalMapper {


     @Autowired
     private ClienteService clienteService;

     @Mapping(source = "idCliente", target = "cliente", qualifiedByName = "getCliente")
     public abstract Animal toEntity(AnimalRequestDTO animalRequestDTO);

     @Named("getCliente")
     Cliente getCliente(@NonNull final Long id){
          return clienteService.obterPorId(id).orElseThrow( ()-> new RegraNegocioException("Cliente não encontrado"));
     }
}
