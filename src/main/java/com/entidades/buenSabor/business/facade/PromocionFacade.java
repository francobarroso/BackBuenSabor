package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.PromocionDto;

import java.util.List;

public interface PromocionFacade extends BaseFacade<PromocionDto, PromocionDto, Long> {
    List<PromocionDto> findBySucursal(Long idSucursal);
    List<PromocionDto> findByEcommerce(Long idSucursal);
}
