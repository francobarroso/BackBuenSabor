package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.PedidoFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.mapper.PedidoMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.PedidoService;
import com.entidades.buenSabor.business.service.ProvinciaService;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoGetDto;
import com.entidades.buenSabor.domain.dto.PedidoShortDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.enums.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PedidoFacadeImp extends BaseFacadeImp<Pedido, PedidoDto, PedidoGetDto, Long> implements PedidoFacade {
    public PedidoFacadeImp(BaseService<Pedido,Long> baseService, BaseMapper<Pedido,PedidoDto,PedidoGetDto> baseMapper){
        super(baseService, baseMapper);
    }
    @Autowired
    PedidoService pedidoService;
    @Autowired
    private PedidoMapper pedidoMapper;

    public PedidoGetDto creation(PedidoDto pedido){

        return baseMapper.toDTO(pedidoService.create(pedido));
    }

    @Override
    public List<PedidoGetDto> findBySucursal(Long idSucursal) {
        return this.pedidoMapper.toDTOsList(this.pedidoService.findBySucursal(idSucursal));
    }

    public List<PedidoShortDto> getByRol(Rol rol, long sucursalId){

        return pedidoService.getByRol(rol,sucursalId);
    }
}