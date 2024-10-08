package com.entidades.buenSabor.domain.dto;

import com.entidades.buenSabor.domain.entities.Cliente;
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
    private Double total;
    private Estado estado;
    private TipoEnvio tipoEnvio;
    private FormaPago formaPago;

    private DomicilioDto domicilio;

    private SucursalDto sucursal;

    private ClienteDto cliente;

    private Set<DetallePedido> detallePedidos = new HashSet<>();

    private EmpleadoDto empleado;
}
