package SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;

public interface IOrderStatusHistoryFactory {

    OrderStatusHistory createHistory(OrderId orderId,
                                     OrderStatus initialStatus,
                                     OrderStatus finalStatus,
                                     StatusChangeReason reason,
                                     ChangedBy changedBy);

    OrderStatusHistory recreateHistory(OrderStatusHistoryId id,
                                       OrderId orderId,
                                       OrderStatus initialStatus,
                                       OrderStatus finalStatus,
                                       StatusChangeReason reason,
                                       ChangedBy changedBy,
                                       StatusChangedMoment moment);
}