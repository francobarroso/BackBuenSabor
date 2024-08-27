package com.entidades.buenSabor.business.service;
import com.lowagie.text.DocumentException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface FacturaService {
    void crearFacturaPdf(Long idPedido, ByteArrayOutputStream outputStream) throws DocumentException, IOException;
}
