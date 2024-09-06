package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.PedidoService;
import com.entidades.buenSabor.business.service.ReporteService;
import com.entidades.buenSabor.repositories.PedidoRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReporteServiceImp implements ReporteService {
    @Autowired
    private PedidoService pedidoService;
    @Override
    public byte[] reporteGanancias(LocalDate startDate, LocalDate endDate, Long idSucursal) throws IOException {
        List<Object[]> ganancias = this.pedidoService.getGananciaByFecha(startDate, endDate,idSucursal);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte de Ganancias");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Crear un estilo de celda con fondo amarillo
        CellStyle yellowCellStyle = workbook.createCellStyle();
        yellowCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        yellowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Crear Encabezado para el rango de fechas
        Row dateRangeRow = sheet.createRow(0);
        Cell cell1 = dateRangeRow.createCell(0);
        cell1.setCellValue("Rango de Fechas:");
        cell1.setCellStyle(yellowCellStyle);

        Cell cell2 = dateRangeRow.createCell(1);
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);
        cell2.setCellValue(formattedStartDate + " a " + formattedEndDate);
        cell2.setCellStyle(yellowCellStyle);

        //Crear Encabezado
        Row headerRow = sheet.createRow(2);
        String[] headers = {"Fecha", "Ganancia"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Llenar datos
        int rowNum = 3;
        for (Object[] ganancia : ganancias) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) ganancia[0]); // Mes (como cadena de texto)
            row.createCell(1).setCellValue((Double) ganancia[1]); // Ganancia (como número)
        }

        // Autosize columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    @Override
    public byte[] reporteProductos(LocalDate startDate, LocalDate endDate, Long idSucursal) throws IOException {
        List<Object[]> productos = this.pedidoService.getProductosByFecha(startDate, endDate,idSucursal);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte de Productos");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Crear un estilo de celda con fondo amarillo
        CellStyle yellowCellStyle = workbook.createCellStyle();
        yellowCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        yellowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Crear Encabezado para el rango de fechas
        Row dateRangeRow = sheet.createRow(0);
        Cell cell1 = dateRangeRow.createCell(0);
        cell1.setCellValue("Rango de Fechas:");
        cell1.setCellStyle(yellowCellStyle);

        Cell cell2 = dateRangeRow.createCell(1);
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);
        cell2.setCellValue(formattedStartDate + " a " + formattedEndDate);
        cell2.setCellStyle(yellowCellStyle);

        //Crear Encabezado
        Row headerRow = sheet.createRow(2);
        String[] headers = {"N°", "Producto", "Cantidad"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Llenar datos
        int position = 1;
        int rowNum = 3;
        for (Object[] producto : productos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(position++); // Posicion en Ranking
            row.createCell(1).setCellValue((String) producto[0]); // Nombre del Producto
            row.createCell(2).setCellValue((int) producto[1]); // Cantidad Vendida
        }

        // Autosize columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }
}
