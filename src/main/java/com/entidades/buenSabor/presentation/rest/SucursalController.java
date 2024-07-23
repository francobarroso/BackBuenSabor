package com.entidades.buenSabor.presentation.rest;


import com.entidades.buenSabor.business.facade.Imp.SucursalFacadeImp;

import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.dto.EmpresaLargeDto;
import com.entidades.buenSabor.domain.dto.SucursalDto;

import com.entidades.buenSabor.domain.entities.Sucursal;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sucursal")
@CrossOrigin("*")
public class SucursalController extends BaseControllerImp<Sucursal, SucursalDto, SucursalDto,Long, SucursalFacadeImp> {
    public SucursalController(SucursalFacadeImp facade) {
        super(facade);
    }

    @GetMapping("/empresa/{id}")
    public ResponseEntity<?> sucursalByEmpresaId(@PathVariable("id") Long idEmpresa){
        return ResponseEntity.ok(facade.sucursalByEmpresaId(idEmpresa));
    }

    @GetMapping("/findByCategorias/{idSucursal}")
    public ResponseEntity<?> findByCategorias(@PathVariable("idSucursal") Long idSucursal){
        return ResponseEntity.ok(facade.findByCategorias(idSucursal));
    }
}
