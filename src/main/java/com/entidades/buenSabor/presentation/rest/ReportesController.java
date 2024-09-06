package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@CrossOrigin("*")
public class ReportesController {
    @Autowired
    private ReporteService reporteService;

    @GetMapping("/ganancias")
    public ResponseEntity<byte[]> reporteGanancia(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                  @RequestParam("idSucursal") Long idSucursal) throws IOException {

        byte[] excelContent = this.reporteService.reporteGanancias(startDate, endDate, idSucursal);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ganancias.xls");
        headers.setContentLength(excelContent.length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelContent);
    }

    @GetMapping("/productos")
    public ResponseEntity<byte[]> reporteProductos(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                  @RequestParam("idSucursal") Long idSucursal) throws IOException {

        byte[] excelContent = this.reporteService.reporteProductos(startDate, endDate, idSucursal);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos.xls");
        headers.setContentLength(excelContent.length);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelContent);
    }
}
