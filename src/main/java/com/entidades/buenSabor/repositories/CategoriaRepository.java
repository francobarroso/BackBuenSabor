package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.Articulo;
import com.entidades.buenSabor.domain.entities.Categoria;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoriaRepository extends BaseRepository<Categoria,Long>{
    @Query("SELECT c FROM Categoria c JOIN c.sucursales s WHERE s.id = :idSucursal")
    List<Categoria> findAllBySucursalId(@Param("idSucursal") Long idSucursal);

    @Query("SELECT a FROM Articulo a WHERE a.categoria.id = :idCategoria")
    List<Articulo> findArticulosByCategoriaId(@Param("idCategoria") Long idCategoria);

}
