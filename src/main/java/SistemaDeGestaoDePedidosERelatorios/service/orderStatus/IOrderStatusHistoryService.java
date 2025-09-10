package SistemaDeGestaoDePedidosERelatorios.service.orderStatus;

import SistemaDeGestaoDePedidosERelatorios.VOs.OrderStatus;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderStatusHistoryService {

    List<OrderStatusHistory> findAll();
    Optional<OrderStatusHistory> findById(UUID historyId);
    List<OrderStatusHistory> findByOrderId(UUID orderId);

    void record(UUID orderId,
                OrderStatus initialStatus,
                OrderStatus finalStatus,
                String reason,
                String changedBy);
}