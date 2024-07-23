package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.entities.Categoria;
import com.entidades.buenSabor.domain.entities.Sucursal;

import java.util.List;

public interface CategoriaService extends BaseService<Categoria, Long> {
    void deleteInSucursales(Long idCategoria, Long idSucursal);

    List<Categoria> findByEmpresa(Long idEmpresa);
}
