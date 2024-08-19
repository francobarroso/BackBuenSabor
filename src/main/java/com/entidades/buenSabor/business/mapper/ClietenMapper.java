package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.ClienteDto;
import com.entidades.buenSabor.domain.entities.Cliente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClietenMapper extends BaseMapper<Cliente, ClienteDto, ClienteDto>{
    ClienteDto toDTO(Cliente source);

    List<ClienteDto> toDTOsList(List<Cliente> source);
}
