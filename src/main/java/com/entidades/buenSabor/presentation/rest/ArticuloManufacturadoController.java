package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.ArticuloManufacturadoFacadeImp;
import com.entidades.buenSabor.domain.dto.ArticuloManufacturadoDto;
import com.entidades.buenSabor.domain.entities.ArticuloManufacturado;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articuloManufacturado")
@CrossOrigin("*")
public class ArticuloManufacturadoController extends BaseControllerImp<ArticuloManufacturado, ArticuloManufacturadoDto, ArticuloManufacturadoDto, Long, ArticuloManufacturadoFacadeImp> {
    public ArticuloManufacturadoController(ArticuloManufacturadoFacadeImp facade){
        super(facade);
    }
    @GetMapping("/findBySucursal/{idSucursal}")
    @PreAuthorize("hasAnyAuthority('superadmin','administrador', 'cajero', 'cocinero')")
    public ResponseEntity<?> findBySucursales(@PathVariable("idSucursal") Long idSucursal) {
        return ResponseEntity.ok(facade.findBySucursales(idSucursal));
    }

    @GetMapping("/paraVenta")
    public ResponseEntity<?> manufacturadosParaVenta() {
        return ResponseEntity.ok(facade.manufacturadosParaVenta());
    }

}