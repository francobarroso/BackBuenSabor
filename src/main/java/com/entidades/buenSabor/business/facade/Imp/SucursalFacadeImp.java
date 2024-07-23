package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.SucursalFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.mapper.SucursalMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.dto.SucursalDto;
import com.entidades.buenSabor.domain.entities.Sucursal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalFacadeImp extends BaseFacadeImp<Sucursal, SucursalDto, SucursalDto,Long> implements SucursalFacade {
    public SucursalFacadeImp(BaseService<Sucursal, Long> baseService, BaseMapper<Sucursal, SucursalDto, SucursalDto> baseMapper) {
        super(baseService, baseMapper);
    }

    @Autowired
    private SucursalMapper sucursalMapper;

    @Autowired
    private SucursalService sucursalService;

    public List<SucursalDto> sucursalByEmpresaId(Long idEmpresa){
        return this.sucursalMapper.toDTOsList(this.sucursalService.sucursalByEmpresaId(idEmpresa));
    }

    @Override
    public List<SucursalDto> findByCategorias(Long idSucursal) {
        return this.sucursalMapper.toDTOsList(this.sucursalService.findByCategorias(idSucursal));
    }


}
