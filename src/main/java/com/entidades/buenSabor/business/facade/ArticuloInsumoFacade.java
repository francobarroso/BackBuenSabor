package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.ArticuloInsumoDto;
import com.entidades.buenSabor.domain.entities.Articulo;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;

import java.util.List;

public interface ArticuloInsumoFacade extends BaseFacade<ArticuloInsumoDto, ArticuloInsumoDto, Long> {
    List<ArticuloInsumoDto> findBySucursales(Long idSucursal);
    List<ArticuloInsumoDto> paraVenta(Long idSucursal);
    List<ArticuloInsumoDto> insumoParaVentaEcommerce();
}
