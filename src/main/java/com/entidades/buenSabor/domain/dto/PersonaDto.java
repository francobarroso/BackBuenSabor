package com.entidades.buenSabor.domain.dto;

import com.entidades.buenSabor.domain.entities.ImagenPersona;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonaDto extends BaseDto {
    private String nombre;
    private String apellido;
    private String telefono;
    @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private UsuarioDto usuario;
}
