package com.entidades.buenSabor.business.facade.Base;

import com.entidades.buenSabor.domain.dto.BaseDto;

import java.io.Serializable;
import java.util.List;

public interface BaseFacade <D extends BaseDto,GD extends BaseDto, ID extends Serializable>{
    public GD createNew(D request);
    public GD getById(Long id);
    public List<GD> getAll();
    public void deleteById(Long id);
    public GD update(D request, Long id);
}
