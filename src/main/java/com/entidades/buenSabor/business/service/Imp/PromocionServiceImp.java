package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.ImagenPromocionService;
import com.entidades.buenSabor.business.service.PromocionDetalleService;
import com.entidades.buenSabor.business.service.PromocionService;
import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.dto.PromocionDto;
import com.entidades.buenSabor.domain.dto.SucursalShortDto;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class PromocionServiceImp extends BaseServiceImp<Promocion, Long> implements PromocionService {

    @Autowired
    ArticuloRepository articuloRepository;

    @Autowired
    ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    PromocionDetalleRepository promocionDetalleRepository;

    @Autowired
    PromocionRepository promocionRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    private ImagenPromocionService imagenPromocionService;

    @Autowired
    private ImagenPromocionRepository imagenPromocionRepository;

    @Autowired
    private SucursalService sucursalService;

    @Override
    public Promocion create(Promocion promocion) {
        Set<Sucursal> sucursales = new HashSet<>();

        for (Sucursal sucursal : promocion.getSucursales()) {
            Sucursal sucursalBd = sucursalRepository.findById(sucursal.getId())
                    .orElseThrow(() -> new RuntimeException("La sucursal con id " + sucursal.getId() + " no se ha encontrado"));

            sucursalBd.getPromociones().add(promocion);
            sucursales.add(sucursalBd);
        }

        promocion.setSucursales(sucursales);

        Set<PromocionDetalle> detalles = promocion.getPromocionDetalles();
        Set<PromocionDetalle> nuevosDetalles = new HashSet<>();

        //Ingresar Articulos Manufacturados a detalles
        if (detalles != null && !detalles.isEmpty()) {
            for (PromocionDetalle detalle : detalles) {
                Articulo articuloExistente = articuloRepository.findById(detalle.getArticulo().getId())
                        .orElseThrow(() -> new RuntimeException("Uno de los artículos enviados no es válido."));
                if (articuloExistente instanceof ArticuloInsumo)
                    articuloExistente = articuloInsumoRepository.findById(detalle.getArticulo().getId())
                            .orElseThrow(() -> new RuntimeException("No se encontro el insumo."));
                else if (articuloExistente instanceof ArticuloManufacturado)
                    articuloExistente = articuloManufacturadoRepository.findById(detalle.getArticulo().getId())
                            .orElseThrow(() -> new RuntimeException("No se encontro el manufacturado."));
                else
                    throw new RuntimeException("El artículo " + detalle.getArticulo().getDenominacion() + " no se ha encontrado.");

                if (articuloExistente == null) {
                    throw new RuntimeException("El artículo " + detalle.getArticulo().getDenominacion() + " no se ha encontrado.");
                }
                PromocionDetalle nuevoDetalle = new PromocionDetalle();
                nuevoDetalle.setCantidad(detalle.getCantidad());
                nuevoDetalle.setArticulo(articuloExistente);
                nuevosDetalles.add(nuevoDetalle);
            }
            promocion.setPromocionDetalles(nuevosDetalles);
        } else {
            throw new RuntimeException("La promoción debe tener al menos un detalle.");
        }

        return super.create(promocion);
    }

    @Transactional
    @Override
    public Promocion update(Promocion promocion, Long id) {
        Promocion promocionExistente = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La promoción con id " + id + " no se ha encontrado"));

        // Actualizar Imágenes
        updateImagenes(promocion, promocionExistente);

        // Actualizar Detalles
        updateDetalles(promocion, promocionExistente);

        // Actualizar Sucursales
        Set<Sucursal> nuevasSucursales = new HashSet<>();
        for (Sucursal sucursal : promocion.getSucursales()) {
            Sucursal sucursalBd = sucursalService.getById(sucursal.getId());
            if (sucursalBd == null) {
                throw new RuntimeException("La sucursal con el id " + sucursal.getId() + " no existe.");
            }

            // Asegurar que la promoción está en la lista de promociones de la sucursal
            if (sucursalBd.getPromociones().stream().noneMatch(prom -> prom.getId().equals(promocionExistente.getId()))) {
                sucursalBd.getPromociones().add(promocionExistente);
                sucursalRepository.save(sucursalBd); // Guardar sucursal actualizada
            }

            nuevasSucursales.add(sucursalBd);
        }

        // Eliminar la promoción de las sucursales que se quitaron
        Set<Sucursal> sucursalesAEliminar = new HashSet<>(promocionExistente.getSucursales());
        sucursalesAEliminar.removeAll(nuevasSucursales);

        for (Sucursal sucursal : sucursalesAEliminar) {
            sucursal.getPromociones().remove(promocionExistente);
            sucursalRepository.save(sucursal); // Guardar sucursal actualizada
        }

        // Asignar las nuevas sucursales a la promoción existente
        promocion.setSucursales(nuevasSucursales);

        return promocionRepository.save(promocion);
    }


    private void updateImagenes(Promocion promocion, Promocion promocionExistente) {
        // Gestión de imágenes
        Set<ImagenPromocion> imagenesExistentes = promocionExistente.getImagenes();
        Set<ImagenPromocion> imagenesNuevas = promocion.getImagenes();
        Set<ImagenPromocion> imagenesPersistidas = new HashSet<>();

        // Eliminar imágenes que no están en el nuevo Articulo Manufacturado
        for (ImagenPromocion imagenExistente : imagenesExistentes) {
            if (imagenesNuevas.stream().noneMatch(imagenNueva -> imagenNueva.getId().equals(imagenExistente.getId()))) {
                String url = imagenExistente.getUrl();
                String publicId = url.substring(url.lastIndexOf("/") + 1);
                //Se eliminad de cloudinary
                this.imagenPromocionService.deleteImage(publicId, imagenExistente.getId().toString());
            }
        }

        // Guardar o mantener imágenes nuevas
        for (ImagenPromocion imagenNueva : imagenesNuevas) {
            if (imagenNueva.getId() != null) {
                Optional<ImagenPromocion> imagenBd = imagenPromocionRepository.findById(imagenNueva.getId());
                imagenBd.ifPresent(imagenesPersistidas::add);
            } else {
                imagenNueva.setEliminado(false);
                ImagenPromocion savedImagen = imagenPromocionRepository.save(imagenNueva);
                imagenesPersistidas.add(savedImagen);
            }
        }

        if (!imagenesPersistidas.isEmpty()) {
            promocion.setImagenes(imagenesPersistidas);
        }
    }

    public void updateDetalles(Promocion promocion, Promocion promocionExistente){
        // Gestión de detalles de articulo manufacturado
        Set<PromocionDetalle> detallesExistentes = promocionExistente.getPromocionDetalles();
        Set<PromocionDetalle> detallesNuevos = promocion.getPromocionDetalles();
        Set<PromocionDetalle> detallesPersistidos = new HashSet<>();

        // Eliminar detalles que no están en el nuevo Articulo Manufacturado
        for (PromocionDetalle detalleExistente : detallesExistentes) {
            if (detallesNuevos.stream().noneMatch(detalleNuevo -> detalleNuevo.getId().equals(detalleExistente.getId()))) {
                promocionDetalleRepository.delete(detalleExistente);
            }
        }

        // Guardar o mantener detalles nuevos
        for (PromocionDetalle detalleNuevo : detallesNuevos) {
            if (detalleNuevo.getId() != null) {
                Optional<PromocionDetalle> detalleBdOptional = promocionDetalleRepository.findById(detalleNuevo.getId());

                detalleBdOptional.ifPresent(detalleBd -> {
                    detalleBd.setCantidad(detalleNuevo.getCantidad());
                    detallesPersistidos.add(detalleBd);
                });
            } else {
                PromocionDetalle savedDetalle = promocionDetalleRepository.save(detalleNuevo);
                detallesPersistidos.add(savedDetalle);
            }
        }

        if (!detallesPersistidos.isEmpty()) {
            for(PromocionDetalle detalle : detallesPersistidos){
                /*if(detalle.isEliminado()){
                    detalle.setEliminado(false);
                }*/
            }

            promocion.setPromocionDetalles(detallesPersistidos);
        }
    }

    @Override
    public void deleteById(Long id) {
        Promocion promocion = this.promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La promoción con id: " + id + " no existe."));

        for(PromocionDetalle detalle : promocion.getPromocionDetalles()){
            PromocionDetalle detalleBd = this.promocionDetalleRepository.findById(detalle.getId())
                    .orElseThrow(() -> new RuntimeException("El detalle no existe"));
            detalleBd.setEliminado(true);
            this.promocionDetalleRepository.save(detalleBd);
        }

        super.deleteById(id);
    }

    @Override
    public List<Promocion> findBySucursal(Long idSucursal) {
        return this.promocionRepository.findAllWithSucursales(idSucursal);
    }

    @Override
    public List<Promocion> findByEcommerce(Long idSucursal) {
        LocalDate fechaActual = LocalDate.now();
        return this.promocionRepository.findByHabilitadoTrueAndFechaHastaGreaterThanEqual(fechaActual, idSucursal);
    }
}
