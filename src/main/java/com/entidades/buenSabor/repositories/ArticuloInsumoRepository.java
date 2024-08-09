package com.entidades.buenSabor.repositories;


import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.Categoria;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloInsumoRepository extends BaseRepository<ArticuloInsumo,Long> {
    @Query("SELECT ai FROM ArticuloInsumo ai " +
            "JOIN ai.categoria c " +
            "JOIN c.sucursales s " +
            "WHERE s.id = :idSucursal " +
            "ORDER BY ai.habilitado DESC")
    List<ArticuloInsumo> findBySucursales(@Param("idSucursal") Long idSucursal);

    @Query("SELECT ai FROM ArticuloInsumo ai JOIN ai.categoria c JOIN c.sucursales s WHERE ai.esParaElaborar = false AND s.id = :idSucursal")
    List<ArticuloInsumo> findAllArticuloInsumosWithEsParaElaborarFalse(@Param("idSucursal") Long idSucursal);

    List<ArticuloInsumo> findByEsParaElaborarIsFalseAndHabilitadoIsTrue();

}