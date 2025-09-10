package SistemaDeGestaoDePedidosERelatorios.assembler;

import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.domain.order.Order;

import java.util.Objects;

public final class OrderDTOAssembler {

    private OrderDTOAssembler() {
    }

    public static OrderResponseDTO toResponseDTO(Order order) {
        Objects.requireNonNull(order, "order");

        final String id = order.getId().getOrderId().toString();
        final String customerName = order.getCustomerName().getCustomerName();
        final String customerEmail = order.getCustomerEmail().getEmail();

        final double orderValue = order.getOrderValue().getOrderValue();

        final String status = order.getStatus().getOrderStatus().name();

        final String createdAt = order.getCreationDate().getCreationDate().toString();

        final String validationMessage;
        if (order.getValidationMessage() == null) {
            validationMessage = null;
        } else {
            validationMessage = order.getValidationMessage().getValidationMessage();
        }

        return new OrderResponseDTO(
                id,
                customerName,
                customerEmail,
                orderValue,
                status,
                createdAt,
                validationMessage
        );
    }
}