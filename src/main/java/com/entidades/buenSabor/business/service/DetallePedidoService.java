package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.entities.DetallePedido;
import com.entidades.buenSabor.domain.entities.PromocionDetalle;

import java.util.List;

public interface DetallePedidoService extends BaseService<DetallePedido, Long> {

    List<DetallePedido> getAllByPedidoId(Long pedidoId);

}
