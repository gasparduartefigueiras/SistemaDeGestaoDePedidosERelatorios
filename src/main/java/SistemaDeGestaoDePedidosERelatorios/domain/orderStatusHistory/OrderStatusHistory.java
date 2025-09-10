package SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;

import java.util.Objects;

public class OrderStatusHistory {

    private final OrderStatusHistoryId id;
    private final OrderId orderId;
    private final OrderStatus initialStatus;
    private final OrderStatus finalStatus;
    private final StatusChangeReason reason;
    private final ChangedBy changedBy;
    private final StatusChangedMoment moment;

    public OrderStatusHistory(OrderStatusHistoryId id,
                              OrderId orderId,
                              OrderStatus initialStatus,
                              OrderStatus finalStatus,
                              StatusChangeReason reason,
                              ChangedBy changedBy,
                              StatusChangedMoment moment) {

        if (id == null){
            throw new IllegalArgumentException("HistoryId cannot be null.");
        }

        if (orderId == null) {
            throw new IllegalArgumentException("OrderId cannot be null.");
        }

        if (initialStatus == null){
            throw new IllegalArgumentException("Initial status cannot be null.");
        }

        if (finalStatus == null){
            throw new IllegalArgumentException("Final status cannot be null.");
        }

        if (changedBy == null){
            throw new IllegalArgumentException("ChangedBy cannot be null.");
        }

        if (moment == null){
            throw new IllegalArgumentException("Moment cannot be null.");
        }

        if (initialStatus.equals(finalStatus)) {
            throw new IllegalArgumentException("Initial and final statuses must differ.");
        }

        this.id = id;
        this.orderId = orderId;
        this.initialStatus = initialStatus;
        this.finalStatus = finalStatus;
        this.reason = reason;
        this.changedBy = changedBy;
        this.moment = moment;
    }

    public OrderStatusHistoryId getId() {
        return id;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderStatus getInitialStatus() {
        return initialStatus;
    }

    public OrderStatus getFinalStatus() {
        return finalStatus;
    }

    public StatusChangeReason getReason() {
        return reason;
    }

    public ChangedBy getChangedBy() {
        return changedBy;
    }

    public StatusChangedMoment getMoment() {
        return moment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderStatusHistory)) return false;
        OrderStatusHistory that = (OrderStatusHistory) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderStatusHistory{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", initialStatus=" + initialStatus +
                ", finalStatus=" + finalStatus +
                ", reason=" + reason +
                ", changedBy=" + changedBy +
                ", moment=" + moment +
                '}';
    }
}