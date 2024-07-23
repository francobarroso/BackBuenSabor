package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.ArticuloInsumoService;
import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.ImagenService;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ArticuloInsumoServiceImp extends BaseServiceImp<ArticuloInsumo,Long> implements ArticuloInsumoService {
    @Autowired
    private ImagenArticuloRepository imagenArticuloRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private PromocionRepository promocionRepository;

    @Override
    public ArticuloInsumo create(ArticuloInsumo articuloInsumo) {

        //Validaciones iniciales
        if(articuloInsumo.getStockActual() < articuloInsumo.getStockMinimo()){
            throw new RuntimeException("El stock actual debe ser mayor al stock minimo.");
        } else if (articuloInsumo.getStockMaximo() < 0 || articuloInsumo.getStockMinimo() < 0) {
            throw new RuntimeException("El stock minimo o maximo debe ser mayor a cero.");
        }

        //Validacion de categorias
        if(articuloInsumo.getCategoria() != null){
            Categoria categoria = categoriaRepository.getById(articuloInsumo.getCategoria().getId());
            if (categoria == null ) {
                throw new RuntimeException("La categoría con id: " + articuloInsumo.getCategoria().getId() + " no existe.");
            }else if(!categoria.isEsInsumo()){
                throw new RuntimeException("La categoria ingresada no es para insumos.");
            }

            articuloInsumo.setCategoria(categoria);
        }else{
            throw new RuntimeException("No se ingreso categoria para el insumo.");
        }

        Set<ImagenArticulo> imagenes = articuloInsumo.getImagenes();
        Set<ImagenArticulo> imagenesPersistidas = new HashSet<>();

        //Validacion de Imagenes
        if(!imagenes.isEmpty()){
            for (ImagenArticulo imagen : imagenes) {
                if (imagen.getId() != null) {
                    Optional<ImagenArticulo> imagenBd = imagenArticuloRepository.findById(imagen.getId());
                    imagenBd.ifPresent(imagenesPersistidas::add);
                } else {
                    imagen.setEliminado(false);
                    ImagenArticulo savedImagen = imagenArticuloRepository.save(imagen);
                    imagenesPersistidas.add(savedImagen);
                }
            }
        }

        if (!imagenesPersistidas.isEmpty()) {
            articuloInsumo.setImagenes(imagenesPersistidas);
        }

        //Validacion Unidad de Medida
        if(articuloInsumo.getUnidadMedida() != null){
            UnidadMedida unidadMedida = this.unidadMedidaRepository.findById(articuloInsumo.getUnidadMedida().getId()).get();
            articuloInsumo.setUnidadMedida(unidadMedida);
        }else{
            throw new RuntimeException("No se ingreso unidad de medida para el insumo.");
        }

        articuloInsumo.setHabilitado(true);

        return super.create(articuloInsumo);
    }

    @Override
    public ArticuloInsumo update(ArticuloInsumo newArticuloInsumo, Long id) {
        ArticuloInsumo articuloInsumoExistente = this.articuloInsumoRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Insumo no encontrado: { id: " + id + " }"));

        //Validaciones iniciales
        if(newArticuloInsumo.getStockActual() < newArticuloInsumo.getStockMinimo()){
            throw new RuntimeException("El stock actual debe ser mayor al stock minimo.");
        } else if (newArticuloInsumo.getStockMaximo() < 0 || newArticuloInsumo.getStockMinimo() < 0) {
            throw new RuntimeException("El stock minimo o maximo debe ser mayor a cero.");
        }

        // Validación de categorías
        if (newArticuloInsumo.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(newArticuloInsumo.getCategoria().getId()).orElseThrow(() ->
                    new RuntimeException("La categoría con id: " + newArticuloInsumo.getCategoria().getId() + " no existe."));
            if (!categoria.isEsInsumo()) {
                throw new RuntimeException("La categoria ingresada no es para insumos.");
            }
            newArticuloInsumo.setCategoria(categoria);
        } else {
            throw new RuntimeException("No se ingreso categoria para el insumo.");
        }

        // Gestión de imágenes
        Set<ImagenArticulo> imagenesExistentes = articuloInsumoExistente.getImagenes();
        Set<ImagenArticulo> imagenesNuevas = newArticuloInsumo.getImagenes();
        Set<ImagenArticulo> imagenesPersistidas = new HashSet<>();

        // Eliminar imágenes que no están en el nuevo ArticuloInsumo
        for (ImagenArticulo imagenExistente : imagenesExistentes) {
            if (imagenesNuevas.stream().noneMatch(imagenNueva -> imagenNueva.getId().equals(imagenExistente.getId()))) {
                String url = imagenExistente.getUrl();
                String publicId = url.substring(url.lastIndexOf("/") + 1);
                //Se eliminad de cloudinary
                this.imagenService.deleteImage(publicId, imagenExistente.getId().toString());
            }
        }

        // Guardar o mantener imágenes nuevas
        for (ImagenArticulo imagenNueva : imagenesNuevas) {
            if (imagenNueva.getId() != null) {
                Optional<ImagenArticulo> imagenBd = imagenArticuloRepository.findById(imagenNueva.getId());
                imagenBd.ifPresent(imagenesPersistidas::add);
            } else {
                imagenNueva.setEliminado(false);
                ImagenArticulo savedImagen = imagenArticuloRepository.save(imagenNueva);
                imagenesPersistidas.add(savedImagen);
            }
        }

        if (!imagenesPersistidas.isEmpty()) {
            newArticuloInsumo.setImagenes(imagenesPersistidas);
        }

        // Validación Unidad de Medida
        if (newArticuloInsumo.getUnidadMedida() != null) {
            UnidadMedida unidadMedida = this.unidadMedidaRepository.findById(newArticuloInsumo.getUnidadMedida().getId()).orElseThrow(() ->
                    new RuntimeException("Unidad de medida no encontrada: { id: " + newArticuloInsumo.getUnidadMedida().getId() + " }"));
            newArticuloInsumo.setUnidadMedida(unidadMedida);
        } else {
            throw new RuntimeException("No se ingreso unidad de medida para el insumo.");
        }

        if(!newArticuloInsumo.isHabilitado()){
            boolean hayDetalles = false;
            boolean hayPromociones = false;

            //Verificar si el insumo tiene detalles asociado
            List<ArticuloManufacturado> insumoEsUtilizado = this.articuloManufacturadoRepository.findByArticuloInsumoId(id);

            for(ArticuloManufacturado manufacturado : insumoEsUtilizado){
                if(manufacturado.isHabilitado()){
                    hayDetalles = true;
                    break;
                }
            }
            if (hayDetalles) {
                throw new RuntimeException("No se puede eliminar el articulo porque está presente en un manufacturado");
            }

            List<Promocion> insumoEnPromocion = this.promocionRepository.findByArticuloInsumoId(id);
            for(Promocion promocion : insumoEnPromocion){
                if(promocion.isHabilitado()){
                    hayPromociones = true;
                    break;
                }
            }

            if (hayPromociones) {
                throw new RuntimeException("No se puede eliminar el articulo porque está presente en una promocion");
            }
        }

        return super.update(newArticuloInsumo, id);
    }


    @Override
    public void deleteById(Long id) {
        ArticuloInsumo insumo = this.articuloInsumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El articulo insumo: " + id + " no existe."));

        boolean hayDetalles = false;

        //Verificar si el insumo tiene detalles asociado
        List<ArticuloManufacturadoDetalle> insumoEsUtilizado = this.articuloManufacturadoDetalleRepository.getByArticuloInsumo(insumo);

        for(ArticuloManufacturadoDetalle detalles : insumoEsUtilizado){
            if(!detalles.isEliminado()){
                hayDetalles = true;
                break;
            }
        }
        if (hayDetalles) {
            throw new RuntimeException("No se puede eliminar el articulo porque está presente en un detalle");
        }

        super.deleteById(id);
    }

    @Override
    public List<ArticuloInsumo> findBySucursales(Long idSucursal) {
        return this.articuloInsumoRepository.findBySucursales(idSucursal);
    }

    @Override
    public List<ArticuloInsumo> paraVenta(Long idSucursal) {
        return this.articuloInsumoRepository.findAllArticuloInsumosWithEsParaElaborarFalse(idSucursal);
    }
}