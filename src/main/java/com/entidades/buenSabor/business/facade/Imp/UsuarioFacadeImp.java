package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.UsuarioFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.mapper.UsuarioMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.UsuarioService;
import com.entidades.buenSabor.domain.dto.UsuarioDto;
import com.entidades.buenSabor.domain.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioFacadeImp extends BaseFacadeImp<Usuario, UsuarioDto,UsuarioDto, Long> implements UsuarioFacade {
    public UsuarioFacadeImp(BaseService<Usuario,Long> baseService, BaseMapper<Usuario, UsuarioDto,UsuarioDto> baseMapper){
        super(baseService, baseMapper);
    }

    @Autowired
    private UsuarioMapper usuarioMapper;
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UsuarioDto findByEmail(String email) {
        return this.usuarioMapper.toDTO(this.usuarioService.findByEmail(email));
    }
}
