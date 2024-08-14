package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.PromocionDto;
import com.entidades.buenSabor.domain.dto.SucursalShortDto;
import com.entidades.buenSabor.domain.entities.Promocion;
import com.entidades.buenSabor.domain.entities.Sucursal;

import java.util.List;
import java.util.Set;

public interface PromocionService extends BaseService<Promocion, Long> {
    Promocion create(Promocion promocion);
    List<Promocion> findBySucursal(Long idSucursal);
    List<Promocion> findByEcommerce();
}
