package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.EmpleadoFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.mapper.EmpleadoMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.EmpleadoService;
import com.entidades.buenSabor.domain.dto.EmpleadoDto;
import com.entidades.buenSabor.domain.entities.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoFacadeImp extends BaseFacadeImp<Empleado, EmpleadoDto,EmpleadoDto, Long> implements EmpleadoFacade {
    public EmpleadoFacadeImp(BaseService<Empleado,Long> baseService, BaseMapper<Empleado,EmpleadoDto,EmpleadoDto> baseMapper){
        super(baseService, baseMapper);
    }

    @Autowired
    private EmpleadoMapper empleadoMapper;

    @Autowired
    private EmpleadoService empleadoService;

    @Override
    public List<EmpleadoDto> findBySucursal(Long idSucursal) {
        return this.empleadoMapper.toDTOsList(this.empleadoService.findBySucursal(idSucursal));
    }
}