package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;
import java.util.UUID;

public class OrderId {

    private final UUID orderId;

    public OrderId() {
        this.orderId = UUID.randomUUID();
    }

    public OrderId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }

        this.orderId = id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderId orderId = (OrderId) o;
        return Objects.equals(this.orderId, orderId.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "OrderId{" + orderId + "}";
    }



}
