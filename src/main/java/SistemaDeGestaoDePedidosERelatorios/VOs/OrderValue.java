package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;

public class OrderValue {

    private final double amount;

    public OrderValue(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        this.amount = Math.round(amount * 100.0) / 100.0;
    }

    public double getOrderValue() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderValue that = (OrderValue) o;
        return Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return String.format("OrderValue: %.2f", amount);
    }
}