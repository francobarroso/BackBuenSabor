package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.Cliente;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends BaseRepository<Cliente,Long> {
    Cliente findByUsuarioEmail(String email);
    boolean existsByUsuarioEmail(String email);
}
