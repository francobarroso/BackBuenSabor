package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.EmpleadoService;
import com.entidades.buenSabor.domain.entities.Empleado;
import com.entidades.buenSabor.domain.entities.Sucursal;
import com.entidades.buenSabor.repositories.EmpleadoRepository;
import com.entidades.buenSabor.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoServiceImp extends BaseServiceImp<Empleado, Long> implements EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private SucursalRepository sucursalRepository;
    @Override
    public List<Empleado> findBySucursal(Long idSucursal) {
        return this.empleadoRepository.findBySucursalId(idSucursal);
    }

    @Override
    public Empleado create(Empleado empleado) {
        Sucursal sucursal = this.sucursalRepository.findById(empleado.getSucursal().getId())
                .orElseThrow(() -> new RuntimeException("La sucursal con id " + empleado.getSucursal().getId() + " no existe."));

        sucursal.getEmpleados().add(empleado);
        empleado.setSucursal(sucursal);

        return super.create(empleado);
    }
}
