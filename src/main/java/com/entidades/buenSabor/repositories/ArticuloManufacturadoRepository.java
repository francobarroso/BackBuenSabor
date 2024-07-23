package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.ArticuloManufacturado;
import com.entidades.buenSabor.domain.entities.Categoria;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloManufacturadoRepository extends BaseRepository<ArticuloManufacturado,Long> {
    @Query("SELECT am FROM ArticuloManufacturado am " +
            "JOIN am.categoria c " +
            "JOIN c.sucursales s " +
            "WHERE s.id = :idSucursal " +
            "ORDER BY am.habilitado DESC")
    List<ArticuloManufacturado> findBySucursales(@Param("idSucursal") Long idSucursal);

    @Query("SELECT am FROM ArticuloManufacturado am JOIN am.articuloManufacturadoDetalles det WHERE det.articuloInsumo.id = :idInsumo")
    List<ArticuloManufacturado> findByArticuloInsumoId(@Param("idInsumo") Long idInsumo);
}
