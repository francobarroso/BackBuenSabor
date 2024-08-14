package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.PromocionFacadeImp;
import com.entidades.buenSabor.domain.dto.PromocionDto;
import com.entidades.buenSabor.domain.entities.Promocion;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promocion")
@CrossOrigin("*")
public class PromocionController extends BaseControllerImp<Promocion, PromocionDto, PromocionDto, Long, PromocionFacadeImp> {
    public PromocionController(PromocionFacadeImp facade){
        super(facade);
    }

    @GetMapping("/findBySucursal/{idSucursal}")
    public ResponseEntity<?> findBySucursal(@PathVariable("idSucursal") Long idSucursal){
        return ResponseEntity.ok(facade.findBySucursal(idSucursal));
    }

    @GetMapping("/findByEcommerce")
    public ResponseEntity<?> findByEcommerce(){
        return ResponseEntity.ok(facade.findByEcommerce());
    }
}
