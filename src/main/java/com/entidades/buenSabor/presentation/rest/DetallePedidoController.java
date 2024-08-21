package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.DetallePedidoFacadeImp;
import com.entidades.buenSabor.business.facade.Imp.PaisFacadeImp;
import com.entidades.buenSabor.domain.dto.PaisDto;
import com.entidades.buenSabor.domain.dto.PedidoDetalleDto;
import com.entidades.buenSabor.domain.entities.DetallePedido;
import com.entidades.buenSabor.domain.entities.Pais;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detallePedido")
@CrossOrigin("*")
public class DetallePedidoController extends BaseControllerImp<DetallePedido, PedidoDetalleDto, PedidoDetalleDto,Long, DetallePedidoFacadeImp> {

    public DetallePedidoController(DetallePedidoFacadeImp facade) {
        super(facade);
    }

    @GetMapping("/byPedidoId/{pedidoId}")
    public ResponseEntity<List<PedidoDetalleDto>> getByPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(facade.findByPedidoId(pedidoId));
    }

}
