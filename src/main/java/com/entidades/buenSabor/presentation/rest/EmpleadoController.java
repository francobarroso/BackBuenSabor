package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.EmpleadoFacadeImp;
import com.entidades.buenSabor.domain.dto.EmpleadoDto;
import com.entidades.buenSabor.domain.entities.Empleado;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import com.entidades.buenSabor.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/empleado")
@CrossOrigin("*")
public class EmpleadoController extends BaseControllerImp<Empleado, EmpleadoDto, EmpleadoDto, Long, EmpleadoFacadeImp> {
    public EmpleadoController(EmpleadoFacadeImp facade){
        super(facade);
    }
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @GetMapping("/findBySucursal/{idSucursal}")
    @PreAuthorize("hasAnyAuthority('superadmin')")
    public ResponseEntity<?> findBySucursal(@PathVariable("idSucursal") Long idSucursal){
        return ResponseEntity.ok(facade.findBySucursal(idSucursal));
    }
    @GetMapping("/findByEmail")
    @PreAuthorize("hasAnyAuthority('administrador', 'cajero', 'cocinero', 'delivery')")
    public ResponseEntity<?> findByEmail(@RequestParam String email){
        return ResponseEntity.ok(facade.findByEmail(email));
    }
}