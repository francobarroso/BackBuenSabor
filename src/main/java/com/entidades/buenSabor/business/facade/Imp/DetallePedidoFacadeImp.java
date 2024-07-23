package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.DetallePedidoFacade;
import com.entidades.buenSabor.business.facade.PaisFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.DetallePedidoService;
import com.entidades.buenSabor.domain.dto.LocalidadDto;
import com.entidades.buenSabor.domain.dto.PaisDto;
import com.entidades.buenSabor.domain.dto.PedidoDetalleDto;
import com.entidades.buenSabor.domain.entities.DetallePedido;
import com.entidades.buenSabor.domain.entities.Pais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DetallePedidoFacadeImp extends BaseFacadeImp<DetallePedido, PedidoDetalleDto,PedidoDetalleDto,  Long> implements DetallePedidoFacade {
    public DetallePedidoFacadeImp(BaseService<DetallePedido, Long> baseService, BaseMapper<DetallePedido, PedidoDetalleDto,PedidoDetalleDto> baseMapper) {
        super(baseService, baseMapper);
    }

    @Autowired
    private DetallePedidoService detallePedidoService;

    public List<PedidoDetalleDto> findByPedidoId(Long id) {

        var entities = detallePedidoService.getAllByPedidoId(id);

        return entities
                .stream()
                .map(baseMapper::toDTO)
                .collect(Collectors.toList());
    }
}
