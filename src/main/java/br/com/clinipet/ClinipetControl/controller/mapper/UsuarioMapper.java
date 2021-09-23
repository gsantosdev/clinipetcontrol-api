package br.com.clinipet.ClinipetControl.controller.mapper;


import br.com.clinipet.ClinipetControl.controller.dto.request.UsuarioRequestDTO;
import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UsuarioMapper {

    public abstract Usuario toEntity (UsuarioRequestDTO usuarioRequestDTO);
}
