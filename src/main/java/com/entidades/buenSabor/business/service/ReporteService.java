package com.entidades.buenSabor.business.service;

import java.io.IOException;
import java.time.LocalDate;

public interface ReporteService {
    byte[] reporteGanancias(LocalDate startDate, LocalDate endDate, Long idSucursal) throws IOException;
    byte[] reporteProductos(LocalDate startDate, LocalDate endDate, Long idSucursal) throws IOException;
}
