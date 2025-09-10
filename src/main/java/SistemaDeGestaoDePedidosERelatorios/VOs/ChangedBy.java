package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;

public class ChangedBy {


    private final String value;

    public ChangedBy(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("ChangedBy cannot be null or empty.");
        }

        String finalValue = value.trim();
        if (finalValue.length() > 20) {
            throw new IllegalArgumentException("ChangedBy too long (max " + 20 + " chars).");
        }
        this.value = finalValue;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangedBy)) return false;
        ChangedBy that = (ChangedBy) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public String toString() { return "ChangedBy{" + value + "}"; }
}