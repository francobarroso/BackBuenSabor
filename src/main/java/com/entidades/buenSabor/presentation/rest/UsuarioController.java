package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.UsuarioFacadeImp;
import com.entidades.buenSabor.domain.dto.UsuarioDto;
import com.entidades.buenSabor.domain.entities.Usuario;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends BaseControllerImp<Usuario, UsuarioDto, UsuarioDto, Long, UsuarioFacadeImp> {
    public UsuarioController(UsuarioFacadeImp facade){
        super(facade);
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<?> findByEmail (@PathVariable("email") String email) {
        return ResponseEntity.ok(facade.findByEmail(email));
    }
}
