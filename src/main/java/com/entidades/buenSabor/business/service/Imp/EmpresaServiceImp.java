package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.EmpresaService;

import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.entities.Empresa;
import com.entidades.buenSabor.domain.entities.Sucursal;
import com.entidades.buenSabor.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpresaServiceImp extends BaseServiceImp<Empresa,Long> implements EmpresaService {

    @Autowired
    SucursalService sucursalService;

    @Autowired
    EmpresaRepository empresaRepository;

    @Override
    public Empresa addSucursal(Long idEmpresa, Long idSucursal) {
        Empresa empresa = empresaRepository.findWithSucursalesById(idEmpresa);
        empresa.getSucursales().add(sucursalService.getById(idSucursal));
        Sucursal sucursal = this.sucursalService.getById(idSucursal);
        sucursal.setEmpresa(empresa);
        this.sucursalService.update(sucursal,idSucursal);
        return empresa;

        
    }

    @Override
    public Empresa findBySucursal(Long idSucursal) {
        return this.empresaRepository.findWithSucursalesById(idSucursal);
    }
}
