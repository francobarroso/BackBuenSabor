package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.ClienteService;
import com.entidades.buenSabor.domain.entities.Cliente;
import com.entidades.buenSabor.domain.entities.Domicilio;
import com.entidades.buenSabor.domain.entities.Localidad;
import com.entidades.buenSabor.repositories.ClienteRepository;
import com.entidades.buenSabor.repositories.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ClienteServiceImp extends BaseServiceImp<Cliente, Long> implements ClienteService {
    @Autowired
    private LocalidadRepository localidadRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente create(Cliente cliente) {
        //Cargar domicilios
        Set<Domicilio> domicilios = new HashSet<>();
        for (Domicilio domicilio: cliente.getDomicilios()){
            Localidad localidad = this.localidadRepository.findById(domicilio.getLocalidad().getId())
                    .orElseThrow(()-> new RuntimeException("No se encontro la localidad"));
            domicilio.setLocalidad(localidad);
            domicilio.setId(null);
            domicilios.add(domicilio);
        }

        cliente.setDomicilios(domicilios);

        return super.create(cliente);
    }

    @Override
    public Cliente findByEmail(String email) {
        return this.clienteRepository.findByUsuarioEmail(email);
    }
}
