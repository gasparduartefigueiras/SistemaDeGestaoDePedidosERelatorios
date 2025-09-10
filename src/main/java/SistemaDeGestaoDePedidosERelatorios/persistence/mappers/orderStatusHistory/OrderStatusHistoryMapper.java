package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.orderStatusHistory;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.IOrderStatusHistoryFactory;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.orderStatusHistory.OrderStatusHistoryDataModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Component
public class OrderStatusHistoryMapper implements IOrderStatusHistoryMapper {

    private final IOrderStatusHistoryFactory orderStatusHistoryFactory;

    public OrderStatusHistoryMapper(IOrderStatusHistoryFactory orderStatusHistoryFactory) {
        this.orderStatusHistoryFactory = Objects.requireNonNull(orderStatusHistoryFactory, "orderStatusHistoryFactory");
    }

    @Override
    public OrderStatusHistoryDataModel toDataModel(OrderStatusHistory orderStatusHistory) {
        Objects.requireNonNull(orderStatusHistory, "orderStatusHistory");

        UUID id = orderStatusHistory.getId().getValue();
        UUID orderId = orderStatusHistory.getOrderId().getOrderId();
        String initialStatus = orderStatusHistory.getInitialStatus().getOrderStatus().name();
        String finalStatus = orderStatusHistory.getFinalStatus().getOrderStatus().name();
        String reason;
        if (orderStatusHistory.getReason() == null) {
            reason = null;
        } else {
            reason = orderStatusHistory.getReason().getReason();
        }        String changedBy = orderStatusHistory.getChangedBy().getValue();
        LocalDateTime date = orderStatusHistory.getMoment().getDateTime();

        return new OrderStatusHistoryDataModel(id, orderId, initialStatus, finalStatus, reason,changedBy,date);

    }

    @Override
    public OrderStatusHistory toDomain(OrderStatusHistoryDataModel orderStatusHistoryDataModel) {

        try {
            Objects.requireNonNull(orderStatusHistoryDataModel, "orderStatusHistoryDataModel");

            OrderStatusHistoryId id = new OrderStatusHistoryId(orderStatusHistoryDataModel.getId());
            OrderId orderId = new OrderId(orderStatusHistoryDataModel.getOrderId());
            OrderStatus initialStatus = new OrderStatus(OrderStatus.Status.valueOf(orderStatusHistoryDataModel.getInitialStatus()));
            OrderStatus finalStatus = new OrderStatus(OrderStatus.Status.valueOf(orderStatusHistoryDataModel.getFinalStatus()));

            StatusChangeReason reason;
            if (orderStatusHistoryDataModel.getReason() == null) {
                reason = null;
            } else {
                reason = new StatusChangeReason(orderStatusHistoryDataModel.getReason());
            }

            ChangedBy changedBy = new ChangedBy(orderStatusHistoryDataModel.getChangedBy());
            StatusChangedMoment moment = new StatusChangedMoment(orderStatusHistoryDataModel.getChangedAt());

            return orderStatusHistoryFactory.recreateHistory(
                    id, orderId, initialStatus, finalStatus, reason, changedBy, moment
            );


        } catch (Exception e) {
            throw new IllegalArgumentException("Error trying to map OrderStatusHistoryDataModel to OrderStatusHistory", e);
        }
    }
}
