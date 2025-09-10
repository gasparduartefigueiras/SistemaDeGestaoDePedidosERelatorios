package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.order;

import SistemaDeGestaoDePedidosERelatorios.domain.order.Order;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.order.OrderDataModel;
import org.springframework.stereotype.Component;

public interface IOrderMapper {

    OrderDataModel toDataModel(Order order);

    Order toDomain(OrderDataModel orderDataModel);
}
