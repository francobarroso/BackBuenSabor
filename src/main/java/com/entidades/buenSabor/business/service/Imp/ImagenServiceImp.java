package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.CloudinaryService;
import com.entidades.buenSabor.business.service.ImagenService;
import com.entidades.buenSabor.domain.entities.ImagenArticulo;
import com.entidades.buenSabor.repositories.ImagenArticuloRepository;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ImagenServiceImp extends BaseServiceImp<ImagenArticulo, Long> implements ImagenService{
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImagenArticuloRepository imageRepository;


    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllImages() {
        try {
            List<ImagenArticulo> images = imageRepository.findAll();
            List<Map<String, Object>> imageList = new ArrayList<>();


            for (ImagenArticulo image : images) {
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("id", image.getId());
                imageMap.put("url", image.getUrl());
                imageList.add(imageMap);
            }


            return ResponseEntity.ok(imageList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Override
    public List<ImagenArticulo> uploadImages(MultipartFile[] files) {
        List<String> urls = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        List<ImagenArticulo> imagenes = new ArrayList<>();
        try {
            for (MultipartFile file : files) {

                ImagenArticulo image = new ImagenArticulo();
                image.setUrl(cloudinaryService.uploadFile(file));
                /*if (image.getUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }*/


                // Agregar la URL a la lista de URLs
                urls.add(image.getUrl());
                image = imageRepository.save(image);
                ids.add(image.getId());
                imagenes.add(image);
            };

            JSONObject responseJson = new JSONObject();
            responseJson.put("status", "OK");
            responseJson.put("urls", urls);
            responseJson.put("ids", ids); // Incluir los IDs en la respuesta


            // Convertir la lista de URLs a un array de strings y devolver como JSON
            //return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            //return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }

        return imagenes;


    }
    @Override
    public ResponseEntity<String> deleteImage(String publicId, String id) {
        Long idImage = Long.parseLong(id);
        try {
            deleteById(idImage);
            return cloudinaryService.deleteImage(publicId, id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }
}