package br.com.clinipet.ClinipetControl.controller.mapper;

import br.com.clinipet.ClinipetControl.controller.dto.request.LancamentoRequestDTO;
import br.com.clinipet.ClinipetControl.exception.RegraNegocioException;
import br.com.clinipet.ClinipetControl.model.entity.Lancamento;
import br.com.clinipet.ClinipetControl.model.entity.Usuario;
import br.com.clinipet.ClinipetControl.service.UsuarioService;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public abstract class LancamentoMapper {

    @Autowired
    private UsuarioService usuarioService;

    @Mapping(source = "idUsuario", target = "usuario", qualifiedByName = "getUsuario")
    public abstract Lancamento toEntity(LancamentoRequestDTO dto);


    @Named("getUsuario")
    Usuario getUsuario(@NonNull final Long id){
        return usuarioService.obterPorId(id).orElseThrow( ()-> new RegraNegocioException("Cliente não encontrado"));
    }
}
