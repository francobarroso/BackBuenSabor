package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.dto.SucursalDto;
import com.entidades.buenSabor.domain.entities.Sucursal;
import com.entidades.buenSabor.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalServiceImp extends BaseServiceImp<Sucursal,Long> implements SucursalService {
    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public Sucursal update(Sucursal request, Long id) {
        Sucursal sucursal = this.sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La sucursal id: " + id + " no existe."));
        request.setCategorias(sucursal.getCategorias());
        return super.update(request, id);
    }

    public List<Sucursal> sucursalByEmpresaId(Long idEmpresa){
        return this.sucursalRepository.findAllByEmpresaId(idEmpresa);
    }

    @Override
    public List<Sucursal> findByCategorias(Long idSucursal) {
        return null;
        //this.sucursalRepository.findByCategorias(idSucursal)
    }
}
