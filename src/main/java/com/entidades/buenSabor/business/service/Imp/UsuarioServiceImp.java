package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.UsuarioService;
import com.entidades.buenSabor.domain.entities.Usuario;
import com.entidades.buenSabor.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImp extends BaseServiceImp<Usuario, Long> implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Override
    public Usuario findByEmail(String email) {
        return this.usuarioRepository.findByEmail(email);
    }
}
