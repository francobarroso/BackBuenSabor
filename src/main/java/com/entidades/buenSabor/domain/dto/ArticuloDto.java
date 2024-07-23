package com.entidades.buenSabor.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ArticuloDto extends BaseDto{
    private String denominacion;
    private Double precioVenta;
    private boolean habilitado;
    private Set<ImagenArticuloDto> imagenes = new HashSet<>();
    private UnidadMedidaDto unidadMedida;
    private CategoriaShortDto categoria;
    private SucursalShortDto sucursal;
}
