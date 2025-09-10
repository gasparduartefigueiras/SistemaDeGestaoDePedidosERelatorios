package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.orderStatusHistory;

import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.orderStatusHistory.OrderStatusHistoryDataModel;

public interface IOrderStatusHistoryMapper {

    OrderStatusHistoryDataModel toDataModel(OrderStatusHistory history);

    OrderStatusHistory toDomain(OrderStatusHistoryDataModel dataModel);
}
