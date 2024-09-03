package com.entidades.buenSabor.config.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    private String destinatario;
    private String asunto;
    private String mensaje;
}
