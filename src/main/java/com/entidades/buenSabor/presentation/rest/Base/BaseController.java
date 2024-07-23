package com.entidades.buenSabor.presentation.rest.Base;

import com.entidades.buenSabor.domain.dto.BaseDto;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public interface BaseController <D extends BaseDto, GD, ID extends Serializable> {
    ResponseEntity<GD> getById(ID id);

    ResponseEntity<List<GD>> getAll();

    ResponseEntity<GD> create(D entity);

    ResponseEntity<GD> edit(D entity, ID id);

    ResponseEntity<?> deleteById(ID id);
}