package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;

import java.util.List;

public interface ArticuloInsumoService extends BaseService<ArticuloInsumo, Long> {
    List<ArticuloInsumo> findBySucursales(Long idSucursal);
    List<ArticuloInsumo> paraVenta(Long idSucursal);
    List<ArticuloInsumo> insumoParaVentaEcommerce();
}
