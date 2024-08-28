package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.entities.Empleado;

import java.util.List;

public interface EmpleadoService extends BaseService<Empleado,Long> {
    List<Empleado> findBySucursal(Long idSucursal);
    Empleado findByEmail(String email);
}
