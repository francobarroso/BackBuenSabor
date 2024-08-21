package com.entidades.buenSabor.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoDetalleDto extends BaseDto{
    private Integer cantidad;
    private Double subTotal;
    private ArticuloDto articulo;
    private PromocionDto promocion;
}
