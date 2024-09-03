package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.*;
import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoShortDto;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.domain.enums.Estado;
import com.entidades.buenSabor.domain.enums.FormaPago;
import com.entidades.buenSabor.domain.enums.Rol;
import com.entidades.buenSabor.domain.enums.TipoEnvio;
import com.entidades.buenSabor.config.email.EmailDto;
import com.entidades.buenSabor.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImp extends BaseServiceImp<Pedido,Long> implements PedidoService {
    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private  PromocionRepository promocionRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private FacturaService facturaService;
    @Autowired
    private EmailService emailService;

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
            if(detalle.getArticulo() != null){
                Articulo articulo = this.articuloRepository.findById(detalle.getArticulo().getId())
                        .orElseThrow(() -> new RuntimeException("El articulo id: " + detalle.getArticulo().getId() + " no existe."));
                descontarStock(articulo, detallePedido.getCantidad());
                detallePedido.setArticulo(articulo);
                detallePedido.setSubTotal(detalle.getCantidad() * articulo.getPrecioVenta());
                detallePedidos.add(detallePedido);

                //Añadir articulos
                articulos.add(articulo);
            }else if(detalle.getPromocion() != null){
                Promocion promocion = this.promocionRepository.findById(detalle.getPromocion().getId())
                        .orElseThrow(() -> new RuntimeException("La promocion id: " + detalle.getPromocion().getId() + " no existe."));
                for(PromocionDetalle promocionDetalle : promocion.getPromocionDetalles()){
                    descontarStock(promocionDetalle.getArticulo(), promocionDetalle.getCantidad()*detallePedido.getCantidad());
                    detallePedido.setPromocion(promocion);
                    detallePedido.setSubTotal(detalle.getCantidad() * promocion.getPrecioPromocional());
                    detallePedidos.add(detallePedido);
                }
            }else {
                throw new RuntimeException("No se han enviando ni articulos ni promociones en el pedido.");
            }

            //Calcular el total del pedido
            total += detallePedido.getSubTotal();
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
        LocalDate fechaActual = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        pedido.setFechaPedido(fechaActual);

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
        return this.pedidoRepository.findBySucursalIdOrderByIdDesc(idSucursal);
    }

    @Override
    public List<Object[]> getGananciaByFecha(LocalDate startDate, LocalDate endDate, Long idSucursal) {
        List<Pedido> pedidos = pedidoRepository.findPedidoByDate(startDate, endDate, idSucursal);

        // Verificar si las fechas están en el mismo mes y año
        boolean sameMonth = startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth();

        if(sameMonth){
            Map<LocalDate, Double> totalesPorDia = pedidos.stream()
                    .collect(Collectors.groupingBy(
                            Pedido::getFechaPedido,
                            Collectors.summingDouble(Pedido::getTotal)
                    ));

            // Crear la lista de resultados en el formato [dd-MM, total]
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
            List<Object[]> resultado = new ArrayList<>();
            for (Map.Entry<LocalDate, Double> entry : totalesPorDia.entrySet()) {
                String fechaFormato = entry.getKey().format(formatter);
                resultado.add(new Object[]{fechaFormato, entry.getValue()});
            }
            return resultado;
        }else{
            Map<Month, Double> totalesPorMes = pedidos.stream()
                    .collect(Collectors.groupingBy(
                            pedido -> pedido.getFechaPedido().getMonth(),
                            Collectors.summingDouble(Pedido::getTotal)
                    ));

            // Crear la lista de resultados en el formato [Nombre del mes, total]
            List<Object[]> resultado = new ArrayList<>();
            for (Month mes : totalesPorMes.keySet()) {
                String nombreMes = mes.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                String nombreMesCapitalizado = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1).toLowerCase();
                Double total = totalesPorMes.get(mes);
                resultado.add(new Object[]{nombreMesCapitalizado, total});
            }


            return resultado;
        }
    }

    @Override
    public List<Object[]> getProductosByFecha(LocalDate startDate, LocalDate endDate, Long idSucursal) {
        List<Pedido> pedidos = pedidoRepository.findPedidoByDate(startDate, endDate, idSucursal);
        Map<String, Integer> articuloVentas = new HashMap<>();

        // Verificar si las fechas están en el mismo mes y año
        boolean sameMonth = startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth();

        // Contar la cantidad de ventas para cada artículo
        for (Pedido pedido : pedidos) {
            for(DetallePedido detalle : pedido.getDetallePedidos()) {
                if(detalle.getArticulo() != null){
                    String nombreArticulo = detalle.getArticulo().getDenominacion();// Asumiendo que puedes acceder al nombre del artículo así
                    articuloVentas.put(nombreArticulo, articuloVentas.getOrDefault(nombreArticulo, 0) + detalle.getCantidad());
                }
            }
        }

        // Ordenar los artículos por cantidad de ventas en orden descendente y obtener los 10 primeros
        List<Map.Entry<String, Integer>> topArticulos = articuloVentas.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());

        // Formatear el resultado en el formato deseado
        List<Object[]> resultado = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : topArticulos) {
            resultado.add(new Object[]{entry.getKey(), entry.getValue()});
        }

        return resultado;
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
    @Transactional
    public void descontarStock(Articulo articulo, Integer cantidad) {
        if(articulo instanceof ArticuloInsumo){
            int stockDescontado = ((ArticuloInsumo) articulo).getStockActual() - cantidad;
            if(stockDescontado <= ((ArticuloInsumo) articulo).getStockMinimo()){
                throw new RuntimeException("El insumo con id " + articulo.getId() + " alcanzó el stock minimo");
            }
            ((ArticuloInsumo) articulo).setStockActual(stockDescontado);
        } else if (articulo instanceof ArticuloManufacturado) {
            Set<ArticuloManufacturadoDetalle> detalles = ((ArticuloManufacturado) articulo).getArticuloManufacturadoDetalles();
            if(detalles != null && !detalles.isEmpty()){
                for(ArticuloManufacturadoDetalle detalle : detalles){
                    ArticuloInsumo insumo = detalle.getArticuloInsumo();
                    int cantidadInsumo = detalle.getCantidad() * cantidad;
                    int stockDescontado = insumo.getStockActual() - cantidadInsumo;
                    if(stockDescontado <= insumo.getStockMinimo()){
                        throw new RuntimeException("El insumo con id " + insumo.getId() + " presente en el articulo "
                        + articulo.getDenominacion() + "(id: " + articulo.getId() + ") alcanzo el stock mínimo");
                    }
                    insumo.setStockActual(stockDescontado);
                }
            }
        }else{
            throw new RuntimeException("Artículo con id: " + articulo.getId() + " no encontrado");
        }
    }

    @Override
    public Pedido update(Pedido pedidoReq, Long id) {
        Pedido pedido = this.pedidoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No se encontro el pedido."));

        if(pedido.getEmpleado() == null){
            pedido.setEmpleado(pedidoReq.getEmpleado());
        }

        // Si el pedido está en proceso no se puede cancelar
        if (pedidoReq.getEstado() == Estado.CANCELADO && pedido.getEstado() != Estado.PENDIENTE) {
            throw new RuntimeException("El pedido no se puede cancelar porque está en proceso");
        }

        // Si el pedido se cancela restaurar stock
        if (pedidoReq.getEstado() == Estado.CANCELADO){
            for(DetallePedido detalle: pedido.getDetallePedidos()){
                if(detalle.getArticulo() != null){
                    Articulo articulo = articuloRepository.findById(detalle.getArticulo().getId()).orElseThrow(() -> new RuntimeException("El artículo con id " + detalle.getArticulo().getId() + " no se ha encontrado."));
                    devolverStock(articulo, detalle.getCantidad());
                    detalle.setArticulo(articulo);
                }else if(detalle.getPromocion() != null){
                    Promocion promocion = this.promocionRepository.findById(detalle.getPromocion().getId())
                            .orElseThrow(() -> new RuntimeException("La promocion id: " + detalle.getPromocion().getId() + " no existe."));
                    for(PromocionDetalle promocionDetalle : promocion.getPromocionDetalles()){
                        devolverStock(promocionDetalle.getArticulo(), promocionDetalle.getCantidad()*detalle.getCantidad());
                        detalle.setPromocion(promocion);
                    }
                }
            }
        }

        // Si el pedido es aprobado, envíar factura
        if (pedidoReq.getEstado() == Estado.PREPARACION) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                // Crear un nuevo documento
                this.facturaService.crearFacturaPdf(id, outputStream);

                EmailDto email = new EmailDto();
                email.setDestinatario(pedido.getCliente().getUsuario().getEmail());
                email.setAsunto("Factura de su pedido #" + id);
                email.setMensaje("¡Gracias por su compra " + pedido.getCliente().getNombre() + "\uD83D\uDE0E\uD83C\uDF54\uD83C\uDF55\uD83C\uDF5F !" +
                        "Le adjuntamos su factura. Saludos!");

                //Enviar el correo con la factura adjunta
                this.emailService.enviarEmail(email, outputStream);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al generar o enviar la factura", e);
            }
        }

        pedido.setEstado(pedidoReq.getEstado());
        return super.update(pedido, id);
    }

    public void devolverStock(Articulo articulo, int cantidad){
        if (articulo instanceof ArticuloInsumo){
            // Si el articulo es un insumo
            int stockAumentado = ((ArticuloInsumo) articulo).getStockActual() + cantidad; // Aumentar cantidad a stock actual
            ((ArticuloInsumo) articulo).setStockActual(stockAumentado); // Asignarle al insumo el stock descontado
        }else if(articulo instanceof ArticuloManufacturado){
            // Obtener los detalles del manufacturado
            Set<ArticuloManufacturadoDetalle> detalles = ((ArticuloManufacturado) articulo).getArticuloManufacturadoDetalles();
            if (detalles != null && !detalles.isEmpty()) {
                for (ArticuloManufacturadoDetalle detalle : detalles) { // Recorrer los detalles
                    ArticuloInsumo insumo = detalle.getArticuloInsumo();
                    int cantidadInsumo = detalle.getCantidad() * cantidad; // Multiplicar la cantidad necesaria de insumo por la cantidad de manufacturados del pedido
                    int stockAumentado = insumo.getStockActual() + cantidadInsumo; // Aumentar el stock actual
                    insumo.setStockActual(stockAumentado); // Asignarle al insumo el stock descontado
                }
            }
        }else{
            throw new RuntimeException("Artículo con id " + articulo.getId() + " no encontrado");
        }

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