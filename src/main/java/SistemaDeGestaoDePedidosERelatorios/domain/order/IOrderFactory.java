package SistemaDeGestaoDePedidosERelatorios.domain.order;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;

public interface IOrderFactory {

    Order createOrder(CustomerName name, CustomerEmail email, OrderValue value);

    Order recreateOrder(CustomerName name, CustomerEmail email, OrderValue value, CreationDate date, OrderId id, OrderStatus status, ValidationMessage message);

}
