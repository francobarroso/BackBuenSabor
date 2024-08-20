package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.mapper.PedidoMapper;
import com.entidades.buenSabor.business.service.ArticuloInsumoService;
import com.entidades.buenSabor.business.service.ArticuloManufacturadoService;
import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.PedidoService;
import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoShortDto;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.domain.enums.Estado;
import com.entidades.buenSabor.domain.enums.FormaPago;
import com.entidades.buenSabor.domain.enums.Rol;
import com.entidades.buenSabor.domain.enums.TipoEnvio;
import com.entidades.buenSabor.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PedidoServiceImp extends BaseServiceImp<Pedido,Long> implements PedidoService {
    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ArticuloManufacturadoService articuloManufacturadoService;

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Override
    public Pedido create(Pedido pedido) {
        //Verificar el tipo de envio y forma de pago
        if(pedido.getTipoEnvio() == TipoEnvio.TAKE_AWAY && pedido.getFormaPago() == FormaPago.MERCADO_PAGO){
            throw new RuntimeException("No se puede pagar con Mercado Pago retirando en el local.");
        }else if(pedido.getTipoEnvio() == TipoEnvio.DELIVERY && pedido.getFormaPago() == FormaPago.EFECTIVO){
            throw new RuntimeException("No se puede pagar en Efectivo en envio por delivery.");
        }

        //Inicializar estado
        if(pedido.getEstado() == null){
            pedido.setEstado(Estado.PENDIENTE);
        }

        //Añadir sucursal
        Sucursal sucursal = this.sucursalRepository.findById(pedido.getSucursal().getId())
                .orElseThrow(() -> new RuntimeException("La sucursal id: " + pedido.getSucursal().getId() + " no existe."));

        pedido.setSucursal(sucursal);

        //Añadir detalles del pedido
        Set<DetallePedido> detallePedidos = new HashSet<>();
        Set<Articulo> articulos = new HashSet<>();
        Double total = 0.;

        for(DetallePedido detalle: pedido.getDetallePedidos()){
            DetallePedido detallePedido = detalle;
            Articulo articulo = this.articuloRepository.findById(detalle.getArticulo().getId())
                    .orElseThrow(() -> new RuntimeException("El articulo id: " + detalle.getArticulo().getId() + " no existe."));
            detallePedido.setArticulo(articulo);
            detallePedido.setSubTotal(detalle.getCantidad() * articulo.getPrecioVenta());
            detallePedidos.add(detallePedido);

            //Calcular el total del pedido
            total += detallePedido.getSubTotal();

            //Añadir articulos
            articulos.add(articulo);
        }

        //Validar total de la venta
        if(!total.equals(pedido.getTotal())){
            System.out.println(total);
            System.out.println(pedido.getTotal());
            throw new RuntimeException("Error: El total del pedido difieren.");
        }

        //Añadir total costo
        pedido.setTotalCosto(totalCosto(detallePedidos));

        //Añadir fecha del pedido
        pedido.setFechaPedido(LocalDate.now());

        //Añadir hora estimada de finalizacion
        pedido.setHoraEstimadaFinalizacion(horaEstimada(articulos));

        //Añadir detalles al pedido
        pedido.setDetallePedidos(detallePedidos);

        return super.create(pedido);
    }

    @Override
    public Pedido create(PedidoDto pedido) {
        return null;
    }

    public List<PedidoShortDto> getByRol(Rol rol, long sucursalId){

        switch (rol){
            case ADMIN:
//                pedidoRepository.getByEstado
                throw new RuntimeException("ROL ERROR");
            case CAJERO:
                return pedidoRepository.getByEstado(0,1,sucursalId);
            case COCINERO:
                return pedidoRepository.getByEstado(2,2,sucursalId);
            case DELIVERY:
                return pedidoRepository.getByEstado(3,3,sucursalId);
            default:
                throw new RuntimeException("ROL ERROR");

        }
    }

    @Override
    public List<Pedido> findBySucursal(Long idSucursal) {
        return this.pedidoRepository.findBySucursalId(idSucursal);
    }

    public Double totalCosto(Set<DetallePedido> detallePedidos){
        Double totalCosto = 0.;

        for(DetallePedido detalles : detallePedidos){
            Articulo articulo = detalles.getArticulo();

            if(articulo instanceof ArticuloManufacturado){
                ArticuloManufacturado articuloManufacturado = this.articuloManufacturadoRepository.findById(articulo.getId()).get();
                for(ArticuloManufacturadoDetalle detalle : articuloManufacturado.getArticuloManufacturadoDetalles()){
                    ArticuloInsumo insumo = detalle.getArticuloInsumo();
                    totalCosto += insumo.getPrecioCompra() * detalle.getCantidad();
                }
            } else if (articulo instanceof ArticuloInsumo) {
                ArticuloInsumo articuloInsumo = this.articuloInsumoRepository.findById(articulo.getId())
                        .orElseThrow(() -> new RuntimeException("El articulo insumo id: " + articulo.getId() + " no existe."));
                totalCosto += articuloInsumo.getPrecioCompra() * detalles.getCantidad();
            }
        }

        return totalCosto;
    }

    public LocalTime horaEstimada(Set<Articulo> articulos){
        Integer minutos = 0;
        LocalTime horaEstimada = LocalTime.now();
        for(Articulo articulo : articulos){
            if(articulo instanceof ArticuloManufacturado){
                minutos += ((ArticuloManufacturado) articulo).getTiempoEstimadoMinutos();
            }
        }

        horaEstimada = horaEstimada.plusMinutes(minutos);

        return horaEstimada;
    }

    @Override
    public Pedido update(Pedido pedido, Long id) {
        if(pedido != null || id != null){
            throw new RuntimeException("No se puede actualizar un pedido.");
        }
        return super.update(pedido, id);
    }

    @Override
    public void deleteById(Long id) {
        Pedido pedido = this.pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El pedido id: " + id + " no existe."));

        if(pedido.getEstado() != Estado.PENDIENTE){
            throw new RuntimeException("El pedido no se puede eliminar porque su estado es distinto a pendiente");
        }

        super.deleteById(id);
    }
}