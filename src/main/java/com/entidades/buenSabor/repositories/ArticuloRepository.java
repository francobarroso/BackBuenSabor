package com.entidades.buenSabor.repositories;


import com.entidades.buenSabor.domain.entities.Articulo;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.ArticuloManufacturado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ArticuloRepository  extends BaseRepository<Articulo, Long> {
    Set<Articulo> findByEliminadoFalse();
}
