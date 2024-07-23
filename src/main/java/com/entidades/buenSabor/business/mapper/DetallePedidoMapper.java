package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.PaisDto;
import com.entidades.buenSabor.domain.dto.PedidoDetalleDto;
import com.entidades.buenSabor.domain.entities.DetallePedido;
import com.entidades.buenSabor.domain.entities.Pais;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetallePedidoMapper extends BaseMapper<DetallePedido, PedidoDetalleDto, PedidoDetalleDto>{
    PedidoDetalleDto toDTO(Pais source);
    public DetallePedido toEntity(PedidoDetalleDto source);
    List<PedidoDetalleDto> toDTOsList(List<DetallePedido> source);
}
