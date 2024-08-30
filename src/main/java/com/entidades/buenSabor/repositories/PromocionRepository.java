package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.ArticuloManufacturado;
import com.entidades.buenSabor.domain.entities.Promocion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromocionRepository extends BaseRepository<Promocion,Long>{
    @Query("SELECT p FROM Promocion p JOIN p.sucursales s WHERE s.id = :idSucursal " +
            "ORDER BY (CASE WHEN p.fechaHasta >= CURRENT_DATE THEN 0 ELSE 1 END), p.fechaHasta DESC")
    List<Promocion> findAllWithSucursales(@Param("idSucursal") Long idSucursal);

    @Query("SELECT p FROM Promocion p JOIN p.promocionDetalles det WHERE det.articulo.id = :idInsumo")
    List<Promocion> findByArticuloInsumoId(@Param("idInsumo") Long idInsumo);

    @Query("SELECT p FROM Promocion p JOIN p.promocionDetalles det WHERE det.articulo.id = :idManufacturado")
    List<Promocion> findByArticuloManufacturadoId(@Param("idManufacturado") Long idManufacturado);
    @Query("SELECT p FROM Promocion p " +
            "JOIN p.sucursales s " +
            "WHERE p.habilitado = true AND p.fechaHasta >= :fechaActual AND s.id = :idSucursal")
    List<Promocion> findByHabilitadoTrueAndFechaHastaGreaterThanEqual(LocalDate fechaActual, @Param("idSucursal") Long idSucursal);

}
