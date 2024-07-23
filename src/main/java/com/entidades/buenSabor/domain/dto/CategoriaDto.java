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
public class CategoriaDto extends BaseDto{
    private String denominacion;
    private boolean esInsumo;
    private Set<SucursalShortDto> sucursales = new HashSet<>();
    private Set<CategoriaDto> subCategorias = new HashSet<>();
}
