package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/factura")
@CrossOrigin("*")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping("/generar/{idPedido}")
    public ResponseEntity<byte[]> generarFactura(@PathVariable("idPedido") Long idPedido) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            this.facturaService.crearFacturaPdf(idPedido, outputStream);

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formattedDate = currentDate.format(formatter);

            // Header de la response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "pedido" + formattedDate + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // Devolver pdf
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}