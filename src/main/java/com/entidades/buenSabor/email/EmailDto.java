package com.entidades.buenSabor.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    private String destinatario;
    private String asunto;
    private String mensaje;
}
