package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoShortDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.enums.Rol;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PedidoService extends BaseService<Pedido, Long> {

    Pedido create(PedidoDto pedido);

    List<PedidoShortDto> getByRol(Rol rol, long sucursalId);

    List<Pedido> findBySucursal(Long idSucursal);
    List<Object[]> getGananciaByFecha(LocalDate startDate, LocalDate endDate);
    List<Object[]> getProductosByFecha(LocalDate startDate, LocalDate endDate);
}
