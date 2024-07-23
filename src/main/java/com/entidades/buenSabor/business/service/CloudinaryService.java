package com.entidades.buenSabor.business.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public interface CloudinaryService {
    default String uploadFile(MultipartFile file) {
        return null;
    }
    ResponseEntity<String> deleteImage(String publicId, String id);
}
