package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.DetallePedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends BaseRepository<DetallePedido,Long>{

    @Query(
            value = "SELECT * FROM DETALLE_PEDIDO dp WHERE dp.PEDIDO_ID =?1",
            nativeQuery = true)
    public List<DetallePedido> getAllByPedidoId(Long pedidoId);
}
