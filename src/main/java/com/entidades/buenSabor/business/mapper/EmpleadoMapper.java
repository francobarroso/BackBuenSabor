package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.EmpleadoDto;
import com.entidades.buenSabor.domain.dto.PromocionDto;
import com.entidades.buenSabor.domain.entities.Empleado;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends BaseMapper<Empleado, EmpleadoDto, EmpleadoDto>{
    EmpleadoDto toDTO(Empleado source);

    Empleado toEntity(PromocionDto source);

    List<EmpleadoDto> toDTOsList(List<Empleado> source);
}
