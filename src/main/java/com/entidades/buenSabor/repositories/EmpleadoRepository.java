package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.Empleado;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends BaseRepository<Empleado,Long> {
    List<Empleado> findBySucursalId(Long SucursalId);

    Empleado findByUsuarioEmail(String email);
}
