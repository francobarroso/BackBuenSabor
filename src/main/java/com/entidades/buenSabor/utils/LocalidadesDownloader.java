package com.entidades.buenSabor.utils;

import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.repositories.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
class LocalidadesDownloader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ImagenPersonaRepository imagenPersonaRepository;
    @Autowired
    private PromocionDetalleRepository promocionDetalleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private ImagenArticuloRepository imagenArticuloRepository;

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) {
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject("https://infra.datos.gob.ar/georef/departamentos.json", String.class);
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray departamentosArray = jsonObject.getJSONArray("departamentos");

// Obtener el país
        Pais pais = paisRepository.findById(1L).orElseGet(() -> {
            Pais newPais = new Pais();
            newPais.setId(1L);
            newPais.setNombre("Argentina");
            return paisRepository.save(newPais);
        });

        departamentosArray.forEach(obj -> {
            JSONObject departamentoJson = (JSONObject) obj;

            Long localidadId = Long.parseLong(departamentoJson.getString("id"));
            String localidadNombre = departamentoJson.getString("nombre");

            JSONObject provinciaJson = departamentoJson.getJSONObject("provincia");
            Long provinciaId = Long.parseLong(provinciaJson.getString("id"));
            String provinciaNombre = provinciaJson.getString("nombre");

            // Verificar si la provincia ya existe por nombre, si no, crearla y guardarla
            Provincia provincia = provinciaRepository.findByNombre(provinciaNombre);
            if (provincia == null) {
                provincia = new Provincia();
                provincia.setId(provinciaId);
                provincia.setNombre(provinciaNombre);
                provincia.setPais(pais);
                provincia = provinciaRepository.save(provincia);
            }

            Localidad localidad = new Localidad();
            localidad.setId(localidadId);
            localidad.setNombre(localidadNombre);
            localidad.setProvincia(provincia);
            localidadRepository.save(localidad);
        });

        

        Empresa empresa1 = Empresa.builder()
                .nombre("Empresa MCC")
                .razonSocial("Razón Social")
                .cuil(1324567890L)
                .build();

        empresaRepository.save(empresa1);

			Domicilio domicilioSucursal = Domicilio.builder()
					.calle("San Martín")
					.numero(123)
					.cp(5515)
					.piso(0)
					.nroDpto(0)
					.localidad(this.localidadRepository.findById(377L).get())
					.build();

        Sucursal sucursal1 = Sucursal.builder()
                .nombre("Sucursal 1 MCC")
                .horarioApertura(LocalTime.of(12,30,0))
                .horarioCierre(LocalTime.of(20,30,0))
                .esCasaMatriz(true)
                .domicilio(domicilioSucursal)
                .empresa(empresa1)
                .build();

        sucursalRepository.save(sucursal1);
        /*
        Set<Sucursal> sucursales = new HashSet<>();
        sucursales.add(sucursal1);

        Categoria categoriaPizza = Categoria.builder()
                .denominacion("Pizza")
                .esInsumo(false)
                .sucursales(sucursales)
                .build();

        Categoria categoriaMolidos = Categoria.builder()
                .denominacion("Molidos")
                .esInsumo(true)
                .sucursales(sucursales)
                .build();

        Categoria categoriaLacteos = Categoria.builder()
                .denominacion("Lacteos")
                .esInsumo(true)
                .sucursales(sucursales)
                .build();

        Categoria categoriaSalsas = Categoria.builder()
                .denominacion("Salsas")
                .esInsumo(true)
                .sucursales(sucursales)
                .build();


        this.categoriaRepository.save(categoriaPizza);
        this.categoriaRepository.save(categoriaMolidos);
        this.categoriaRepository.save(categoriaLacteos);
        this.categoriaRepository.save(categoriaSalsas);

        Categoria categoriaHamburguesa = Categoria.builder()
                .denominacion("Hamburguesas")
                .esInsumo(false)
                .sucursales(sucursales)
                .build();

        Categoria categoriaCarnes = Categoria.builder()
                .denominacion("Carnes")
                .esInsumo(false)
                .sucursales(sucursales)
                .build();

        Categoria categoriaFrescos = Categoria.builder()
                .denominacion("Frescos")
                .esInsumo(false)
                .sucursales(sucursales)
                .build();

        this.categoriaRepository.save(categoriaHamburguesa);
        this.categoriaRepository.save(categoriaCarnes);
        this.categoriaRepository.save(categoriaFrescos);

        UnidadMedida unidadMedidaGramos = UnidadMedida.builder()
                .denominacion("Gramos")
                .build();

        this.unidadMedidaRepository.save(unidadMedidaGramos);

        ImagenArticulo imgHarina = ImagenArticulo.builder()
                .url("https://st2.depositphotos.com/1000348/6439/i/450/depositphotos_64399139-stock-photo-flour-and-wheat-ears.jpg")
                .build();

        Set<ImagenArticulo> imgHarinas = new HashSet<>();
        imgHarinas.add(imgHarina);

        ArticuloInsumo harina = ArticuloInsumo.builder()
                .denominacion("Harina")
                .precioCompra(300.)
                .imagenes(imgHarinas)
                .unidadMedida(unidadMedidaGramos)
                .categoria(categoriaMolidos)
                .stockActual(20)
                .stockMaximo(100)
                .stockMinimo(1)
                .esParaElaborar(true)
                .build();

        ImagenArticulo imgQueso = ImagenArticulo.builder()
                .url("https://i0.wp.com/petroantillana.com/wp-content/uploads/2021/09/queso-gouda-acheese-1.jpg?fit=%2C&ssl=1")
                .build();

        Set<ImagenArticulo> imgQuesos = new HashSet<>();
        imgQuesos.add(imgQueso);

        ArticuloInsumo queso = ArticuloInsumo.builder()
                .denominacion("Queso")
                .precioCompra(500.)
                .imagenes(imgQuesos)
                .unidadMedida(unidadMedidaGramos)
                .categoria(categoriaLacteos)
                .stockActual(50)
                .stockMaximo(200)
                .stockMinimo(1)
                .esParaElaborar(true)
                .build();

        ImagenArticulo imgSalsa = ImagenArticulo.builder()
                .url("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsL8frGGGyuFiVUUHKimulAYhW5F2VJvXu-g&s")
                .build();

        Set<ImagenArticulo> imgSalsas = new HashSet<>();
        imgSalsas.add(imgSalsa);

        ArticuloInsumo salsaTomate = ArticuloInsumo.builder()
                .denominacion("Salsa de tomate")
                .precioCompra(250.)
                .imagenes(imgSalsas)
                .unidadMedida(unidadMedidaGramos)
                .categoria(categoriaSalsas)
                .stockActual(60)
                .stockMaximo(150)
                .stockMinimo(1)
                .esParaElaborar(true)
                .build();

        this.articuloInsumoRepository.save(harina);
        this.articuloInsumoRepository.save(queso);
        this.articuloInsumoRepository.save(salsaTomate);


        ImagenArticulo imgCarneMolida = ImagenArticulo.builder()
                .url("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRd16u1Ud_zxyc_fO_A1OC4ywJoftP4nDKt7g&s")
                .build();

        Set<ImagenArticulo> imgCarneMolidas = new HashSet<>();
        imgCarneMolidas.add(imgCarneMolida);

        ArticuloInsumo carneMolida = ArticuloInsumo.builder()
                .denominacion("Carne Molida")
                .precioCompra(300.)
                .imagenes(imgCarneMolidas)
                .unidadMedida(unidadMedidaGramos)
                .categoria(categoriaCarnes)
                .stockActual(20)
                .stockMaximo(100)
                .stockMinimo(1)
                .esParaElaborar(true)
                .build();

        ImagenArticulo imgTomate = ImagenArticulo.builder()
                .url("https://bigarsa.com.ar/wp-content/uploads/2024/04/pic_1549256667_1549256706.jpg")
                .build();

        Set<ImagenArticulo> imgTomates = new HashSet<>();
        imgTomates.add(imgTomate);

        ArticuloInsumo tomate = ArticuloInsumo.builder()
                .denominacion("Tomate")
                .precioCompra(300.)
                .imagenes(imgTomates)
                .unidadMedida(unidadMedidaGramos)
                .categoria(categoriaFrescos)
                .stockActual(20)
                .stockMaximo(100)
                .stockMinimo(1)
                .esParaElaborar(true)
                .build();

        this.articuloInsumoRepository.save(carneMolida);
        this.articuloInsumoRepository.save(tomate);


        ImagenArticulo imgPizza = ImagenArticulo.builder()
                .url("https://media.istockphoto.com/id/1442417585/es/foto/persona-recibiendo-un-pedazo-de-pizza-de-pepperoni-con-queso.jpg?s=612x612&w=0&k=20&c=Uk4fj96OIDxE4v2S5sRRXRY_gZ899_TE6jGD-T-TysI=")
                .build();

        Set<ImagenArticulo> imgPizzas = new HashSet<>();
        imgPizzas.add(imgPizza);

        ArticuloManufacturadoDetalle detalle1 = ArticuloManufacturadoDetalle.builder()
                .cantidad(1)
                .articuloInsumo(harina)
                .build();
        ArticuloManufacturadoDetalle detalle2 = ArticuloManufacturadoDetalle.builder()
                .cantidad(2)
                .articuloInsumo(queso)
                .build();
        ArticuloManufacturadoDetalle detalle3 = ArticuloManufacturadoDetalle.builder()
                .cantidad(2)
                .articuloInsumo(salsaTomate)
                .build();

        Set<ArticuloManufacturadoDetalle> detalles = new HashSet<>();
        detalles.add(detalle1);
        detalles.add(detalle2);
        detalles.add(detalle3);

        ArticuloManufacturado articuloManufacturado = ArticuloManufacturado.builder()
                .denominacion("Pizza Napolitana")
                .precioVenta(5000.)
                .imagenes(imgPizzas)
                .unidadMedida(unidadMedidaGramos)
                .categoria(categoriaPizza)
                .descripcion("Pizza con abundante queso.")
                .tiempoEstimadoMinutos(15)
                .preparacion("Colocar abundante salsa y queso y dejar 10 minutos en el horno.")
                .articuloManufacturadoDetalles(detalles)
                .build();

        this.articuloManufacturadoRepository.save(articuloManufacturado);


        ImagenArticulo imgHamburguesa = ImagenArticulo.builder()
                .url("https://resizer.glanacion.com/resizer/v2/hamburguesa-blt-de-john-john-burger-bacon-lettuce-RHVGX3MHVRB7VGEXBEVCCZHW5I.jpg?auth=53776ee5a203ae1fbe457df3473f7c2d9470016ad51a458c73c5b950966a4f57&width=768&quality=70&smart=false")
                .build();

        Set<ImagenArticulo> imgHamburguesas = new HashSet<>();
        imgHamburguesas.add(imgHamburguesa);

        ArticuloManufacturadoDetalle detalleH1 = ArticuloManufacturadoDetalle.builder()
                .cantidad(1)
                .articuloInsumo(harina)
                .build();
        ArticuloManufacturadoDetalle detalleH2 = ArticuloManufacturadoDetalle.builder()
                .cantidad(2)
                .articuloInsumo(queso)
                .build();
        ArticuloManufacturadoDetalle detalleH3 = ArticuloManufacturadoDetalle.builder()
                .cantidad(2)
                .articuloInsumo(tomate)
                .build();
        ArticuloManufacturadoDetalle detalleH4 = ArticuloManufacturadoDetalle.builder()
                .cantidad(2)
                .articuloInsumo(carneMolida)
                .build();

        Set<ArticuloManufacturadoDetalle> detallesH = new HashSet<>();
        detallesH.add(detalleH1);
        detallesH.add(detalleH2);
        detallesH.add(detalleH3);
        detallesH.add(detalleH4);

        ArticuloManufacturado articuloManufacturadoH = ArticuloManufacturado.builder()
                .denominacion("Hamburguesa con Queso")
                .precioVenta(6000.)
                .imagenes(imgHamburguesas)
                .unidadMedida(unidadMedidaGramos)
                .categoria(categoriaHamburguesa)
                .descripcion("Hamburguesa con queso, tomate y lechuga.")
                .tiempoEstimadoMinutos(10)
                .preparacion("Cocinar carne y armar la hamburguesa.")
                .articuloManufacturadoDetalles(detallesH)
                .build();

        this.articuloManufacturadoRepository.save(articuloManufacturadoH);

        ImagenPersona imagenCliente = ImagenPersona.builder()
                .url("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAboyCmArPxbW8rOU6NKGEfdDyT-M_2PNLXw&s")
                .build();

        Domicilio domicilioCliente = Domicilio.builder()
                .calle("San Martín")
                .numero(1025)
                .cp(5501)
                .piso(0)
                .nroDpto(0)
                .localidad(this.localidadRepository.findById(326L).get())
                .build();

        Cliente cliente = Cliente.builder()
                .nombre("Juan")
                .apellido("Perez")
                .fechaNacimiento(LocalDate.of(2004,4,2))
                .imagenPersona(imagenCliente)
                .telefono("2616683926")
                .build();

        cliente.getDomicilios().add(domicilioCliente);

        this.clienteRepository.save(cliente);
        ImagenPersona imagenEmpleado = ImagenPersona.builder()
                .url("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAboyCmArPxbW8rOU6NKGEfdDyT-M_2PNLXw&s")
                .build();

        Empleado empleado = Empleado.builder()
                .nombre("Pedro")
                .apellido("Gonzalez")
                .fechaNacimiento(LocalDate.of(2003,6,12))
                .imagenPersona(imagenEmpleado)
                .telefono("2615583344")
                .sucursal(sucursal1)
                .build();

        this.empleadoRepository.save(empleado);

        */
    }
}