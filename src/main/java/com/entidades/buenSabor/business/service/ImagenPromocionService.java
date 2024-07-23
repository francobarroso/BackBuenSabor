package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.entities.ImagenArticulo;
import com.entidades.buenSabor.domain.entities.ImagenPromocion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImagenPromocionService extends BaseService<ImagenPromocion, Long> {
    ResponseEntity<List<Map<String, Object>>> getAllImages();
    List<ImagenPromocion> uploadImages(MultipartFile[] files);
    ResponseEntity<String> deleteImage(String publicId, String id);
}
