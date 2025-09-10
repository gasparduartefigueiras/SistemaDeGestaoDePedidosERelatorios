package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;

public class OrderStatus {

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    private final Status orderStatus;

    public OrderStatus (Status order) {
        if (order == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.orderStatus = order;
    }

    public static OrderStatus pending() {
        return new OrderStatus(Status.PENDING);
    }

    public static OrderStatus approved() {
        return new OrderStatus(Status.APPROVED);
    }

    public static OrderStatus rejected() {
        return new OrderStatus(Status.REJECTED);
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatus that = (OrderStatus) o;
        return orderStatus == that.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatus);
    }

    @Override
    public String toString() {
        return "OrderStatus{" + orderStatus + "}";
    }
}
