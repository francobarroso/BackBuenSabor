package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.PaisDto;
import com.entidades.buenSabor.domain.dto.PedidoDetalleDto;

import java.util.List;

public interface DetallePedidoFacade extends BaseFacade<PedidoDetalleDto, PedidoDetalleDto, Long> {

    List<PedidoDetalleDto> findByPedidoId(Long id);

}
