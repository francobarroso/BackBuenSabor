package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.ProvinciaDto;
import com.entidades.buenSabor.domain.dto.UnidadMedidaDto;
import com.entidades.buenSabor.domain.entities.Provincia;
import com.entidades.buenSabor.domain.entities.UnidadMedida;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnidadMedidaMapper extends BaseMapper<UnidadMedida, UnidadMedidaDto,UnidadMedidaDto>{
    UnidadMedidaDto toDTO(Provincia source);
    public UnidadMedida toEntity(UnidadMedidaDto source);
    List<UnidadMedidaDto> toDTOsList(List<UnidadMedida> source);
}
