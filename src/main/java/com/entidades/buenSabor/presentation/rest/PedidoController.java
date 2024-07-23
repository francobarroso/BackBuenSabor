package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.PedidoFacadeImp;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoGetDto;
import com.entidades.buenSabor.domain.dto.PedidoShortDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.enums.Rol;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
@CrossOrigin("*")
public class PedidoController extends BaseControllerImp<Pedido, PedidoDto, PedidoGetDto, Long, PedidoFacadeImp> {
    public PedidoController(PedidoFacadeImp facade){
        super(facade);
    }

    @PostMapping("crear")
    public ResponseEntity<PedidoGetDto> crear(@RequestBody PedidoDto entity){
        return ResponseEntity.ok(facade.creation(entity));
    }

    @GetMapping("/byRol/{rol}/{sucursalId}")
    public ResponseEntity<List<PedidoShortDto>> getByPedidoId(@PathVariable Rol rol, @PathVariable long sucursalId) {
        return ResponseEntity.ok(facade.getByRol(rol, sucursalId));
    }
}