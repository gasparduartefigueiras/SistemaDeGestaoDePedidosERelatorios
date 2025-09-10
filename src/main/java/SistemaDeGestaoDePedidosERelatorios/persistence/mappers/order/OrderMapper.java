package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.order;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.domain.order.IOrderFactory;
import SistemaDeGestaoDePedidosERelatorios.domain.order.Order;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.order.OrderDataModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Component
public class OrderMapper implements IOrderMapper{

    private final IOrderFactory orderFactory;


    public OrderMapper(IOrderFactory orderFactory) {
        this.orderFactory = Objects.requireNonNull(orderFactory, "orderFactory");
    }

    @Override
    public OrderDataModel toDataModel(Order order) {
        Objects.requireNonNull(order, "order");

        UUID id = order.getId().getOrderId();
        String customerName = order.getCustomerName().getCustomerName();
        String customerEmail = order.getCustomerEmail().getEmail();
        double orderValue = order.getOrderValue().getOrderValue();
        LocalDate creationDate = order.getCreationDate().getCreationDate();
        String status = order.getStatus().getOrderStatus().name();

        String validationMessage;
        if (order.getValidationMessage() != null) {
            validationMessage = order.getValidationMessage().getValidationMessage();
        } else {
            validationMessage = null;
        }

        return new OrderDataModel(id, customerName, customerEmail, orderValue, creationDate, status, validationMessage
        );
    }

    @Override
    public Order toDomain(OrderDataModel orderDataModel) {

        try {

            Objects.requireNonNull(orderDataModel, "orderDataModel");

            CustomerName name = new CustomerName(orderDataModel.getCustomerName());
            CustomerEmail email = new CustomerEmail(orderDataModel.getCustomerEmail());
            OrderValue value = new OrderValue(orderDataModel.getOrderValue());
            CreationDate date = new CreationDate(orderDataModel.getCreationDate());
            OrderId id = new OrderId(orderDataModel.getId());

            OrderStatus status = new OrderStatus(OrderStatus.Status.valueOf(orderDataModel.getStatus()));

            ValidationMessage message;
            if (orderDataModel.getValidationMessage() != null) {
                message = new ValidationMessage(orderDataModel.getValidationMessage());
            } else {
                message = null;
            }

            return orderFactory.recreateOrder(name, email, value, date, id, status, message);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error trying to map OrderDataModel to Order", e);
        }
    }
}