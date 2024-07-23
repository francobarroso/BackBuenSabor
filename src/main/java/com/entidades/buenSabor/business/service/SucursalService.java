package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.SucursalDto;
import com.entidades.buenSabor.domain.entities.Sucursal;

import java.util.List;

public interface SucursalService  extends BaseService<Sucursal, Long> {
    List<Sucursal> sucursalByEmpresaId(Long idEmpresa);
    List<Sucursal>findByCategorias(Long idSucursal);
}

