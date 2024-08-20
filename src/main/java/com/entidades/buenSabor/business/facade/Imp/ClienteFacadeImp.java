package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.ClienteFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.mapper.ClienteMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.ClienteService;
import com.entidades.buenSabor.domain.dto.ClienteDto;
import com.entidades.buenSabor.domain.entities.Cliente;
import com.entidades.buenSabor.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteFacadeImp extends BaseFacadeImp<Cliente, ClienteDto,ClienteDto, Long> implements ClienteFacade {
    public ClienteFacadeImp(BaseService<Cliente,Long> baseService, BaseMapper<Cliente, ClienteDto,ClienteDto> baseMapper){
        super(baseService, baseMapper);
    }

    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private ClienteService clienteService;

    @Override
    public ClienteDto findByEmail(String email) {
        return this.clienteMapper.toDTO(this.clienteService.findByEmail(email));
    }
}