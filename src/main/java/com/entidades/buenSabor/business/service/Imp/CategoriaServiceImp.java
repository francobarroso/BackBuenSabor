package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.CategoriaService;
import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.repositories.ArticuloInsumoRepository;
import com.entidades.buenSabor.repositories.ArticuloManufacturadoRepository;
import com.entidades.buenSabor.repositories.CategoriaRepository;
import com.entidades.buenSabor.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
public class CategoriaServiceImp extends BaseServiceImp<Categoria, Long> implements CategoriaService {
    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public Categoria create(Categoria categoria) {
        Set<Sucursal> sucursales = new HashSet<>();

        if (categoria.getSucursales() != null && !categoria.getSucursales().isEmpty()) {
            for (Sucursal sucursal : categoria.getSucursales()) {
                Sucursal sucursalAux = this.sucursalService.getById(sucursal.getId());
                if (sucursalAux == null) {
                    throw new RuntimeException("La sucursal con el id " + sucursal.getId() + " no existe.");
                }
                sucursalAux.getCategorias().add(categoria);
                sucursales.add(sucursalAux);
            }
        }

        categoria.setSucursales(sucursales);

        //categoria.setCategoriaPadre(categoria);
        if (!categoria.getSubCategorias().isEmpty()) {
            mapearSubcategorias(categoria, sucursales);
        }

        return super.create(categoria);
    }

    private void mapearSubcategorias(Categoria categoria, Set<Sucursal> sucursales) {
        if (!categoria.getSubCategorias().isEmpty()) {
            for (Categoria subcategoria : categoria.getSubCategorias()) {
                subcategoria.setCategoriaPadre(categoria);
                subcategoria.setSucursales(sucursales);
                subcategoria.setEsInsumo(categoria.isEsInsumo());
                for (Sucursal sucursal : sucursales) {
                    sucursal.getCategorias().add(subcategoria);
                }
                if (!subcategoria.getSubCategorias().isEmpty()) {
                    mapearSubcategorias(subcategoria, sucursales);
                }
            }
        }
    }

    @Override
    public Categoria update(Categoria newCategoria, Long id) {
        try {
            Categoria categoriaExistente = this.categoriaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("La categoria con el id: " + id + " no existe."));


            categoriaExistente.setDenominacion(newCategoria.getDenominacion());
            categoriaExistente.setEsInsumo(newCategoria.isEsInsumo());

            // Actualizar sucursales
            Set<Sucursal> sucursales = new HashSet<>();
            for (Sucursal sucursal : newCategoria.getSucursales()) {
                Sucursal sucursalBd = this.sucursalService.getById(sucursal.getId());
                if (sucursalBd == null) {
                    throw new RuntimeException("La sucursal con el id " + sucursal.getId() + " no existe.");
                }
                boolean categoriaExists = sucursalBd.getCategorias().stream()
                        .anyMatch(cat -> cat.getId() != null && cat.getId().equals(newCategoria.getId()));

                if (!categoriaExists) {
                    sucursalBd.getCategorias().add(newCategoria);
                }
                sucursales.add(sucursalBd);
            }

            // Actualizar la relación de sucursales de la categoría existente
            categoriaExistente.setSucursales(sucursales);

            // Manejar subcategorías
            actualizarSubcategorias(categoriaExistente, newCategoria, sucursales, newCategoria.isEsInsumo());

            return this.categoriaRepository.save(categoriaExistente);
        } catch (Exception e) {
            throw e;
        }
    }

    private void actualizarSubcategorias(Categoria categoriaExistente, Categoria newCategoria, Set<Sucursal> sucursales, boolean esInsumo){
        if (newCategoria.getSubCategorias() != null && !newCategoria.getSubCategorias().isEmpty()){
            for(Categoria subcategoriaNueva: newCategoria.getSubCategorias()){
                Optional<Categoria> subcategoriaExistenteOpt = categoriaExistente.getSubCategorias().stream()
                        .filter(sc -> sc.getId().equals(subcategoriaNueva.getId()))
                        .findFirst();

                if (subcategoriaExistenteOpt.isPresent()) {
                    Categoria subcategoriaExistente = subcategoriaExistenteOpt.get();
                    subcategoriaExistente.setDenominacion(subcategoriaNueva.getDenominacion());
                    subcategoriaExistente.setEsInsumo(esInsumo);  // Propagar valor de newCategoria
                    subcategoriaExistente.setSucursales(sucursales);
                    for (Sucursal sucursal : sucursales) {
                        boolean categoriaExists = sucursal.getCategorias().stream()
                                .anyMatch(cat -> cat.getId() != null && cat.getId().equals(subcategoriaExistente.getId()));

                        if (!categoriaExists) {
                            sucursal.getCategorias().add(subcategoriaExistente);
                        }
                    }
                    actualizarSubcategorias(subcategoriaExistente, subcategoriaNueva, sucursales, esInsumo);
                } else {
                    subcategoriaNueva.setCategoriaPadre(categoriaExistente);
                    subcategoriaNueva.setSucursales(sucursales);
                    subcategoriaNueva.setEsInsumo(esInsumo);  // Propagar valor de newCategoria
                    categoriaExistente.getSubCategorias().add(subcategoriaNueva);

                    // Guardar la nueva subcategoría antes de agregarla a las sucursales
                    Categoria savedSubcategoriaNueva = categoriaRepository.save(subcategoriaNueva);

                    for (Sucursal sucursal : sucursales) {
                        sucursal.getCategorias().add(savedSubcategoriaNueva);
                    }
                    actualizarSubcategorias(savedSubcategoriaNueva, subcategoriaNueva, sucursales, esInsumo);
                }
            }
        }
    }

    public void deleteInSucursales(Long idCategoria, Long idSucursal) {
        Categoria categoriaExistente = this.categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("La categoría con el id: " + idCategoria + " no existe."));

        List<Articulo> articulosAsociados = this.categoriaRepository.findArticulosByCategoriaId(idCategoria);

        if(articulosAsociados != null && !articulosAsociados.isEmpty()){
            throw new RuntimeException("No se puede dar de baja esta categoria, tiene articulos asociados.");
        }

        Sucursal sucursal = this.sucursalService.getById(idSucursal);

        //Eliminar realcion entre sucursal y categoria
        sucursal.getCategorias().remove(categoriaExistente);
        categoriaExistente.getSucursales().remove(sucursal);

        if(!categoriaExistente.getSubCategorias().isEmpty()){
            for(Categoria subCategoria : categoriaExistente.getSubCategorias()){
                deleteInSucursales(subCategoria.getId(), idSucursal);
            }
        }

        this.categoriaRepository.save(categoriaExistente);
    }

    @Override
    public List<Categoria> findByEmpresa(Long idEmpresa) {
        return this.categoriaRepository.findAllBySucursalId(idEmpresa);
    }

    @Override
    public List<Categoria> findByEcommerce() {
        List<Categoria> result = new ArrayList<>();
        result.addAll(this.categoriaRepository.findCategoriaInsumos());
        result.addAll(this.categoriaRepository.findCategoriaManufacturados());
        return result;
    }


    @Override
    public void deleteById(Long id) {
        Categoria categoria = this.categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoría con el id: " + id + " no existe."));

        List<Articulo> articulosAsociados = this.categoriaRepository.findArticulosByCategoriaId(id);

        if(articulosAsociados != null && !articulosAsociados.isEmpty()){
            throw new RuntimeException("No se puede eliminar esta categoria, tiene articulos asociados.");
        }

        eliminarSubcategorias(categoria);

        this.categoriaRepository.save(categoria);
    }

    private void eliminarSubcategorias(Categoria categoria){
        //Quitar categoria padre
        if(categoria.getCategoriaPadre() != null){
            categoria.setCategoriaPadre(null);
        }
        baseRepository.delete(categoria);
        if (!categoria.getSubCategorias().isEmpty()){
            //Quita relacion con sucursal
            for(Sucursal sucursal : categoria.getSucursales() ){
                deleteInSucursales(categoria.getId(), sucursal.getId());
            }
            for (Categoria subcategoria: categoria.getSubCategorias())
                eliminarSubcategorias(subcategoria);
        }
    }
}