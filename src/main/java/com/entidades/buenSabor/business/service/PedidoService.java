package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoShortDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.enums.Rol;

import java.util.List;

public interface PedidoService extends BaseService<Pedido, Long> {

    Pedido create(PedidoDto pedido);

    List<PedidoShortDto> getByRol(Rol rol, long sucursalId);
}
