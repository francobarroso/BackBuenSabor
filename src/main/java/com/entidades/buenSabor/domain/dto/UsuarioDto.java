package com.entidades.buenSabor.domain.dto;

import com.entidades.buenSabor.domain.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioDto extends BaseDto{
    private String auth0Id;
    private String email;
    private Rol rol;
}
