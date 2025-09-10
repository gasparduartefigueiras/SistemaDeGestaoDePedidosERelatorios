package SistemaDeGestaoDePedidosERelatorios.domain.order;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory implements IOrderFactory {


    @Override
    public Order createOrder(CustomerName name, CustomerEmail email, OrderValue value) {
        if (name == null || email == null || value == null) {
            throw new IllegalArgumentException("Name, Email and OrderValue cannot be null.");
        }

        OrderId id = new OrderId();
        CreationDate date = new CreationDate();
        OrderStatus status = OrderStatus.pending();
        ValidationMessage message = null;

        return new Order (id, name, email, value, date, status, message);
    }


    @Override
    public Order recreateOrder(CustomerName name, CustomerEmail email, OrderValue value, CreationDate date, OrderId id, OrderStatus status, ValidationMessage message) {
        return new Order(id, name, email, value, date, status, message);
    }
}
