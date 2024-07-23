package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.BaseDto;
import com.entidades.buenSabor.domain.entities.Base;

import java.util.List;

public interface BaseMapper<E extends Base,D extends BaseDto, GetDto extends BaseDto>{
    public GetDto toDTO(E source);
    public E toEntity(D source);
    public List<GetDto> toDTOsList(List<E> source);
}
