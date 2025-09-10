package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;
import java.util.UUID;

public class OrderStatusHistoryId {

    private final UUID id;

    public OrderStatusHistoryId() {
        this.id = UUID.randomUUID();
    }

    public OrderStatusHistoryId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("OrderStatusHistoryId cannot be null.");
        }
        this.id = id;
    }

    public UUID getValue() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderStatusHistoryId)) return false;
        OrderStatusHistoryId that = (OrderStatusHistoryId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderStatusHistoryId{" + id + "}";
    }
}