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
public class CategoriaGetDto extends BaseDto{
    private String denominacion;
    private boolean esInsumo;
    private Set<SucursalShortDto> sucursales;
    private Set<CategoriaDto> subCategorias;
    private CategoriaShortDto categoriaPadre;
}
