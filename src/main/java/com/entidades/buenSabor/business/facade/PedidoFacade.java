package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoGetDto;
import com.entidades.buenSabor.domain.entities.Pedido;

import java.util.List;
import java.util.Set;

public interface PedidoFacade extends BaseFacade<PedidoDto, PedidoGetDto, Long> {

    PedidoGetDto creation(PedidoDto pedido);

    List<PedidoGetDto> findBySucursal(Long idSucursal);
}
