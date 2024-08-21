package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.dto.PedidoShortDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends BaseRepository<Pedido,Long>{
    @Query(value = "SELECT p FROM Pedido p ORDER BY p.id DESC LIMIT 1")
    Pedido findLastPedido();

    @Query(value = "SELECT  ID, FECHA_PEDIDO, HORA_ESTIMADA_FINALIZACION, FORMA_PAGO, TIPO_ENVIO, TOTAL, ESTADO FROM PEDIDO WHERE ELIMINADO = FALSE AND ESTADO = ?1 AND ESTADO = ?2 AND SUCURSAL_ID =?3",nativeQuery = true)
    List<PedidoShortDto> getByEstado(int rol1, int rol2, long sucursalId);

    List<Pedido> findBySucursalId(Long idSucursal);

    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido BETWEEN :startDate AND :endDate AND p.estado = com.entidades.buenSabor.domain.enums.Estado.PENDIENTE")
    List<Pedido> findPedidoByDate(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.fechaPedido BETWEEN :startDate AND :endDate AND p.estado = com.entidades.buenSabor.domain.enums.Estado.PENDIENTE")
    Double sumTotalBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
