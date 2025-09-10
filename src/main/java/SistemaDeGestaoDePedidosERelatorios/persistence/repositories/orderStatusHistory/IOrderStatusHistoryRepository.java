package SistemaDeGestaoDePedidosERelatorios.persistence.repositories.orderStatusHistory;

import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.orderStatusHistory.OrderStatusHistoryDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface IOrderStatusHistoryRepository extends JpaRepository<OrderStatusHistoryDataModel, UUID> {

    List<OrderStatusHistoryDataModel> findByOrderIdOrderByChangedAtAsc(UUID orderId);
}