package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.entities.Usuario;

public interface UsuarioService extends BaseService<Usuario, Long> {
    Usuario findByEmail(String email);
}
