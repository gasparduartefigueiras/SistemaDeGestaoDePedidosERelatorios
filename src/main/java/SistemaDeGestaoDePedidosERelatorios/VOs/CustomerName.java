package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;

public class CustomerName {

    private final String customerName;

    public CustomerName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        this.customerName = name.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerName that = (CustomerName) o;
        return Objects.equals(customerName, that.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerName);
    }

    @Override
    public String toString() {
        return "CustomerName{" + customerName + "}";
    }
}
