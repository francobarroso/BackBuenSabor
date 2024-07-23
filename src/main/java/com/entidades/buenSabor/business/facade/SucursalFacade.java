package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.SucursalDto;

import java.util.List;

public interface SucursalFacade extends BaseFacade<SucursalDto, SucursalDto, Long> {
    List<SucursalDto> sucursalByEmpresaId(Long idEmpresa);
    List<SucursalDto> findByCategorias(Long idSucursal);
}
