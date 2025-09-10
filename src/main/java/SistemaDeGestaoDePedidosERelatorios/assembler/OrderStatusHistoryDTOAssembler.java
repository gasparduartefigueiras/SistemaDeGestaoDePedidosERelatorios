package SistemaDeGestaoDePedidosERelatorios.assembler;

import SistemaDeGestaoDePedidosERelatorios.DTOs.orderStatusHistory.OrderStatusHistoryResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistory;

import java.time.ZoneOffset;

public class OrderStatusHistoryDTOAssembler {

    private OrderStatusHistoryDTOAssembler() {
    }

    public static OrderStatusHistoryResponseDTO toResponseDTO(OrderStatusHistory orderStatusHistory) {
        String id = null;
        String orderId = null;
        String initialStatus = null;
        String finalStatus = null;
        String reason = null;
        String changedBy = null;
        java.time.Instant changedAt = null;

        if (orderStatusHistory.getId() != null) {
            id = orderStatusHistory.getId().getValue().toString();
        }

        if (orderStatusHistory.getOrderId() != null) {
            orderId = orderStatusHistory.getOrderId().getOrderId().toString();
        }

        if (orderStatusHistory.getInitialStatus() != null) {
            initialStatus = orderStatusHistory.getInitialStatus().getOrderStatus().name();
        }

        if (orderStatusHistory.getFinalStatus() != null) {
            finalStatus = orderStatusHistory.getFinalStatus().getOrderStatus().name();
        }

        if (orderStatusHistory.getReason() != null) {
            reason = orderStatusHistory.getReason().getReason();
        }

        if (orderStatusHistory.getChangedBy() != null) {
            changedBy = orderStatusHistory.getChangedBy().getValue();
        }

        if (orderStatusHistory.getMoment() != null && orderStatusHistory.getMoment().getDateTime() != null) {
            changedAt = orderStatusHistory.getMoment().getDateTime().toInstant(ZoneOffset.UTC);
        }

        return new OrderStatusHistoryResponseDTO(
                id,
                orderId,
                initialStatus,
                finalStatus,
                reason,
                changedBy,
                changedAt
        );
    }
}