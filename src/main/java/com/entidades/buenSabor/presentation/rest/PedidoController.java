package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.PedidoFacadeImp;
import com.entidades.buenSabor.business.service.PedidoService;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoGetDto;
import com.entidades.buenSabor.domain.dto.PedidoShortDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.enums.Rol;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import com.entidades.buenSabor.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@CrossOrigin("*")
public class PedidoController extends BaseControllerImp<Pedido, PedidoDto, PedidoGetDto, Long, PedidoFacadeImp> {
    public PedidoController(PedidoFacadeImp facade){
        super(facade);
    }

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping("crear")
    public ResponseEntity<PedidoGetDto> crear(@RequestBody PedidoDto entity){
        return ResponseEntity.ok(facade.creation(entity));
    }

    @GetMapping("/byRol/{rol}/{sucursalId}")
    public ResponseEntity<List<PedidoShortDto>> getByPedidoId(@PathVariable Rol rol, @PathVariable long sucursalId) {
        return ResponseEntity.ok(facade.getByRol(rol, sucursalId));
    }

    @GetMapping("/findBySucursal/{idSucursal}")
    public ResponseEntity<?> findBySucursal(@PathVariable("idSucursal") Long idSucursal){
        return ResponseEntity.ok(facade.findBySucursal(idSucursal));
    }

    @GetMapping("/gananciaByFecha")
    public List<Object[]> getGananciaByFecha(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return this.pedidoService.getGananciaByFecha(startDate, endDate);
    }

    @GetMapping("/productosByFecha")
    public List<Object[]> getProductosByFecha(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return this.pedidoService.getProductosByFecha(startDate, endDate);
    }

    @GetMapping("/totalByFecha")
    public Double getTotalByFecha(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return this.pedidoRepository.sumTotalBetweenDates(startDate, endDate);
    }
}