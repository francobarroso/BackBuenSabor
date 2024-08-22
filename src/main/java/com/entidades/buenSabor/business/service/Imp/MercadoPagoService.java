package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.entities.PreferenceMp;
import com.entidades.buenSabor.repositories.PedidoRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public PreferenceMp getPreferenciaIdMercadoPago(PedidoDto pedidoDto) {
        Pedido pedido = pedidoRepository.findLastPedido();
        pedido.setEstado(pedidoDto.getEstado());

        try {
            // Configuración del Access Token
            MercadoPagoConfig.setAccessToken("APP_USR-6868831087152965-060612-d9acf8e76d3b35850e81882d8b6ff7a0-1844671669");

            // Crear item de la preferencia
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(String.valueOf(pedido.getId()))
                    .title("Carrito de compras del Buen Sabor")
                    .description("Su pedido estara listo a las " + pedido.getHoraEstimadaFinalizacion() + "aproximadamente.")
                    .pictureUrl("https://www.bupasalud.com/sites/default/files/inline-images/bupa_598072389.jpg")
                    .categoryId("art")
                    .quantity(1)
                    .currencyId("ARS")
                    .unitPrice(new BigDecimal(pedido.getTotal()))
                    .build();
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            // URLs de redirección
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:5174/")
                    .failure("http://localhost:5174/pedido")
                    .pending("http://localhost:5174/pedido")
                    .build();

            // Crear la solicitud de preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .build();

            // Cliente de Mercado Pago
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            // Crear y devolver el objeto PreferenceMp
            PreferenceMp mpPreference = new PreferenceMp();
            mpPreference.setStatusCode(preference.getResponse().getStatusCode());
            mpPreference.setId(preference.getId());

            this.pedidoRepository.save(pedido);

            return mpPreference;

        } catch (MPApiException apiException) {
            System.err.println("Error de API: " + apiException.getApiResponse().getContent());
            apiException.printStackTrace();
            return null;
        } catch (MPException e) {
            e.printStackTrace();
            return null;
        }
    }
}
