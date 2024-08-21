package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.service.ImagenService;
import com.entidades.buenSabor.domain.entities.ImagenArticulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/imagen/articulo")
@CrossOrigin("*")
public class ImagenController {
    @Autowired
    private ImagenService imagenService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('superadmin','administrador')")
    public List<ImagenArticulo> uploadImages(
            @RequestParam(value = "uploads", required = true) MultipartFile[] files) {
        try {
            return this.imagenService.uploadImages(files);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/deleteImg")
    @PreAuthorize("hasAnyAuthority('superadmin','administrador')")
    public ResponseEntity<String> deleteById(@RequestParam("publicId") String publicId, @RequestParam("id") String id) {
        try {
            return this.imagenService.deleteImage(publicId, id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid UUID format");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred");
        }
    }

    //TRAE TODAS LAS IMAGENES EN LA TABLA IMAGENES
    @GetMapping("/getImages")
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        try {
            return this.imagenService.getAllImages();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };
}