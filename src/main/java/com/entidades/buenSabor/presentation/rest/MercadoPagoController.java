package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.service.Imp.MercadoPagoService;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.entities.PreferenceMp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mercadoPago")
@CrossOrigin("*")
public class MercadoPagoController {
    @Autowired
    private MercadoPagoService mercadoPagoService;
    @PostMapping("/crearPreferenceMp")
    public PreferenceMp crearPreferenceMp(@RequestBody PedidoDto pedido){
        try{
            PreferenceMp preferenceMp = this.mercadoPagoService.getPreferenciaIdMercadoPago(pedido);
            return preferenceMp;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
