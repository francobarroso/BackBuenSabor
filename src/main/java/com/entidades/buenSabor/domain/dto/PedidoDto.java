package com.entidades.buenSabor.domain.dto;

import com.entidades.buenSabor.domain.entities.DetallePedido;
import com.entidades.buenSabor.domain.enums.Estado;
import com.entidades.buenSabor.domain.enums.FormaPago;
import com.entidades.buenSabor.domain.enums.TipoEnvio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoDto extends BaseDto{
    private Estado estado;
    private TipoEnvio tipoEnvio;
    private FormaPago formaPago;

    private DomicilioDto domicilio;

    private Long sucursalId;

    private Long clienteId;

    private Set<DetallePedido> detallePedidos = new HashSet<>();

    private Long empleadoId;
}
