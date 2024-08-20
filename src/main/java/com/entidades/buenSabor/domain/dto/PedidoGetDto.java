package com.entidades.buenSabor.domain.dto;

import com.entidades.buenSabor.domain.enums.Estado;
import com.entidades.buenSabor.domain.enums.FormaPago;
import com.entidades.buenSabor.domain.enums.TipoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoGetDto extends BaseDto{
    @Schema(type = "string", format = "time", pattern = "HH:mm:ss")
    private LocalTime horaEstimadaFinalizacion;
    private Double total;
    private Double totalCosto;
    private Estado estado;
    private TipoEnvio tipoEnvio;
    private FormaPago formaPago;
    @Schema(type = "string", format = "date", pattern = "yyyy-MM-dd")
    private LocalDate fechaPedido;
    private DomicilioDto domicilio;
    private SucursalDto sucursal;
    private ClienteDto cliente;
    private Set<PedidoDetalleDto> detallePedidos = new HashSet<>();
    private EmpleadoDto empleado;
}
