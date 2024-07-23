package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoGetDto;
import com.entidades.buenSabor.domain.entities.Pedido;

public interface PedidoFacade extends BaseFacade<PedidoDto, PedidoGetDto, Long> {

    PedidoGetDto creation(PedidoDto pedido);
}
