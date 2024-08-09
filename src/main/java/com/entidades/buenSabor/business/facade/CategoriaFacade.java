package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.CategoriaDto;
import com.entidades.buenSabor.domain.dto.CategoriaGetDto;
import com.entidades.buenSabor.domain.dto.SucursalDto;

import java.util.List;

public interface CategoriaFacade extends BaseFacade<CategoriaDto, CategoriaGetDto, Long> {
    List<CategoriaGetDto> findByEmpresa(Long idEmpresa);

    List<CategoriaGetDto> findByEcommerce();
}
