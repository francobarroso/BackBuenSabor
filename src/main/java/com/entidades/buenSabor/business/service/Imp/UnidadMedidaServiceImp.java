package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.UnidadMedidaService;
import com.entidades.buenSabor.domain.entities.Articulo;
import com.entidades.buenSabor.domain.entities.UnidadMedida;
import com.entidades.buenSabor.repositories.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class UnidadMedidaServiceImp extends BaseServiceImp<UnidadMedida, Long> implements UnidadMedidaService {
    @Autowired
    private ArticuloRepository articuloRepository;
    @Override
    public void deleteById(Long id) {
        Set<Articulo> articulos = this.articuloRepository.findByEliminadoFalse();
        for(Articulo articulo : articulos) {
            if(Objects.equals(articulo.getUnidadMedida().getId(), id))
                throw new RuntimeException("No se puede eliminar la unidad de medida, esta presente en un articulo");
        }
        super.deleteById(id);
    }
}
