package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.UsuarioDto;
import com.entidades.buenSabor.domain.entities.Usuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends BaseMapper<Usuario, UsuarioDto, UsuarioDto>{
    UsuarioDto toDTO(Usuario source);

    List<UsuarioDto> toDTOsList(List<Usuario> source);
}
