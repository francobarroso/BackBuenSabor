package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.dto.PedidoShortDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends BaseRepository<Pedido,Long>{
    @Query(value = "SELECT p FROM Pedido p ORDER BY p.id DESC LIMIT 1")
    Pedido findLastPedido();

    @Query(value = "SELECT  ID, FECHA_PEDIDO, HORA_ESTIMADA_FINALIZACION, FORMA_PAGO, TIPO_ENVIO, TOTAL, ESTADO FROM PEDIDO WHERE ELIMINADO = FALSE AND ESTADO = ?1 AND ESTADO = ?2 AND SUCURSAL_ID =?3",nativeQuery = true)
    List<PedidoShortDto> getByEstado(int rol1, int rol2, long sucursalId);
}
