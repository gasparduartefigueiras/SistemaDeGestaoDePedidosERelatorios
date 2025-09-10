package SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusHistoryFactory implements IOrderStatusHistoryFactory {

    @Override
    public OrderStatusHistory createHistory(OrderId orderId,
                                            OrderStatus initialStatus,
                                            OrderStatus finalStatus,
                                            StatusChangeReason reason,
                                            ChangedBy changedBy) {
        OrderStatusHistoryId id = new OrderStatusHistoryId();
        StatusChangedMoment moment = new StatusChangedMoment();
        return new OrderStatusHistory(id, orderId, initialStatus, finalStatus, reason, changedBy, moment);
    }

    @Override
    public OrderStatusHistory recreateHistory(OrderStatusHistoryId id,
                                              OrderId orderId,
                                              OrderStatus initialStatus,
                                              OrderStatus finalStatus,
                                              StatusChangeReason reason,
                                              ChangedBy changedBy,
                                              StatusChangedMoment moment) {
        return new OrderStatusHistory(id, orderId, initialStatus, finalStatus, reason, changedBy, moment);
    }
}