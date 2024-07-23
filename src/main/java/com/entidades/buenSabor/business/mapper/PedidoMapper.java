package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoGetDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper extends BaseMapper<Pedido, PedidoDto, PedidoGetDto>{
    PedidoGetDto toDTO(Pedido source);
    Pedido toEntity(PedidoGetDto source);
    List<PedidoGetDto> toDTOsList(List<Pedido> source);
}
