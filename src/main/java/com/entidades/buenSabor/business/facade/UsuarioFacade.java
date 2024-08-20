package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.UsuarioDto;

public interface UsuarioFacade extends BaseFacade<UsuarioDto,UsuarioDto,Long> {
    UsuarioDto findByEmail(String email);
}
