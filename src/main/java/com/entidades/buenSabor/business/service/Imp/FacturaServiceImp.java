package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.FacturaService;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.domain.enums.TipoEnvio;
import com.entidades.buenSabor.repositories.PedidoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

@Service
public class FacturaServiceImp extends BaseServiceImp<Factura,Long> implements FacturaService{
    @Autowired
    private PedidoRepository pedidoRepository;

    protected static Font titulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 14);
    protected static Font texto = FontFactory.getFont(FontFactory.TIMES, 11);
    protected static Font textoBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 11);


    @Override
    public void crearFacturaPdf(Long idPedido, ByteArrayOutputStream outputStream) throws DocumentException, IOException {
        Pedido pedido = this.pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        //Configurar el encabezado
        configureHeader(document);

        //Información de la empresa y sucursal
        addEmpresaInfo(document, pedido);
        addSucursalInfo(document, pedido);

        //Información del pedido
        addPedidoInfo(document, pedido);

        //Información del cliente
        addClienteInfo(document, pedido);

        //Detalle del pedido
        addDetallePedido(document, pedido);

        //Total y descuento
        addTotal(document, pedido);

        document.close();
    }

    public void configureHeader(Document document) throws DocumentException, MalformedURLException, IOException{
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);

        Image logo = Image.getInstance("https://res.cloudinary.com/dbglrv5xe/image/upload/logo_p4eutb");
        logo.scalePercent(10f);

        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell titleCell = new PdfPCell(new Phrase("EL BUEN SABOR", titulo));
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(logoCell);
        table.addCell(titleCell);

        document.add(table);
        addEmptyLine(document, 1);
    }

    public void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }

    public void addEmpresaInfo(Document document, Pedido pedido) throws DocumentException {
        Empresa empresa = pedido.getSucursal().getEmpresa();
        String empresaExist = empresa != null ? empresa.getNombre() : "";
        document.add(new Paragraph(empresaExist, textoBold));
        addEmptyLine(document, 1);
    }

    public void addSucursalInfo(Document document, Pedido pedido) throws DocumentException {
        Sucursal sucursal = pedido.getSucursal();
        String sucursalExist = sucursal != null ? sucursal.getNombre() : "";
        document.add(new Paragraph(sucursalExist, textoBold));

        Domicilio domicilio = sucursal.getDomicilio();
        String domicilioExist = domicilio != null ? domicilio.getCalle() : "";

        Localidad localidad = domicilio.getLocalidad();
        String localidadExist = localidad != null ? localidad.getNombre() : "";
        document.add(new Paragraph(domicilioExist +  " " + localidadExist, texto));

        addEmptyLine(document, 1);
    }

    public void addPedidoInfo(Document document, Pedido pedido) throws DocumentException {
        document.add(new Paragraph("N° de pedido: " + pedido.getId(), texto));
        document.add(new Paragraph("Fecha del pedido: " + pedido.getFechaPedido(), texto));
        addEmptyLine(document, 1);
    }

    public void addClienteInfo(Document document, Pedido pedido) throws DocumentException {
        Cliente cliente = pedido.getCliente();
        String clienteNombre = cliente != null ? cliente.getNombre() : "";
        String clienteApellido = cliente != null ? cliente.getApellido() : "";
        document.add(new Paragraph("Cliente: " + clienteNombre + " " + clienteApellido, texto));

        String formaPago = pedido != null ? String.valueOf(pedido.getFormaPago()) : "";
        document.add(new Paragraph("Método de pago: " + formaPago, texto));
        addEmptyLine(document, 1);
    }

    public void addDetallePedido(Document document, Pedido pedido) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{1, 3, 2, 2});
        table.setWidthPercentage(100);

        addTableHeader(table, new String[]{"Código", "Articulo", "Cantidad", "Subtotal"});

        pedido.getDetallePedidos().forEach(detalle -> {
            if (detalle.getArticulo() != null) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(detalle.getArticulo().getId()), texto)));
                table.addCell(new PdfPCell(new Phrase(detalle.getArticulo().getDenominacion(), texto)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(detalle.getCantidad()), texto)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf("$" + detalle.getSubTotal()), texto)));
            } else if (detalle.getPromocion() != null) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(detalle.getPromocion().getId()), texto)));
                table.addCell(new PdfPCell(new Phrase(detalle.getPromocion().getDenominacion(), texto)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(detalle.getCantidad()), texto)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf("$" + detalle.getSubTotal()), texto)));
            }
        });

        document.add(table);
        addEmptyLine(document, 1);
    }

    public void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, textoBold));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }
    }

    public void addTotal(Document document, Pedido pedido) throws DocumentException {
        document.add(new Paragraph("Total: $" + pedido.getTotal(), texto));
    }
}
