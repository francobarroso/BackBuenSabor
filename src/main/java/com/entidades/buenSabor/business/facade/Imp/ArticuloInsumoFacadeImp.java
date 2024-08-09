package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.ArticuloInsumoFacade;
import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.mapper.ArticuloInsumoMapper;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.ArticuloInsumoService;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.ArticuloInsumoDto;
import com.entidades.buenSabor.domain.dto.DomicilioDto;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.Domicilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloInsumoFacadeImp extends BaseFacadeImp<ArticuloInsumo, ArticuloInsumoDto, ArticuloInsumoDto,Long> implements ArticuloInsumoFacade {
    public ArticuloInsumoFacadeImp(BaseService<ArticuloInsumo, Long> baseService, BaseMapper<ArticuloInsumo, ArticuloInsumoDto, ArticuloInsumoDto> baseMapper){
        super(baseService, baseMapper);
    }

    @Autowired
    private ArticuloInsumoMapper articuloInsumoMapper;

    @Autowired
    private ArticuloInsumoService articuloInsumoService;

    @Override
    public List<ArticuloInsumoDto> findBySucursales(Long idSucursal) {
        return this.articuloInsumoMapper.toDTOsList(this.articuloInsumoService.findBySucursales(idSucursal));
    }

    @Override
    public List<ArticuloInsumoDto> paraVenta(Long idSucursal) {
        return this.articuloInsumoMapper.toDTOsList(this.articuloInsumoService.paraVenta(idSucursal));
    }

    @Override
    public List<ArticuloInsumoDto> insumoParaVentaEcommerce() {
        return this.articuloInsumoMapper.toDTOsList(this.articuloInsumoService.insumoParaVentaEcommerce());
    }
}
