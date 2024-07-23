package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.EmpleadoDto;

import java.util.List;

public interface EmpleadoFacade extends BaseFacade<EmpleadoDto,EmpleadoDto,Long> {
    List<EmpleadoDto> findBySucursal(Long idSucursal);
}
