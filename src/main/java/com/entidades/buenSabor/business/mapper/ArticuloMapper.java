package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.ArticuloDto;
import com.entidades.buenSabor.domain.entities.Articulo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticuloMapper extends BaseMapper<Articulo, ArticuloDto, ArticuloDto> {
    @Override
    Articulo toEntity(ArticuloDto source);
}
