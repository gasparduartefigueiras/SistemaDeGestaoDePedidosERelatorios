package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;

public class StatusChangeReason {

    private final String reason;

    public StatusChangeReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Reason cannot be null or empty.");
        }
        if (reason.length() > 500) {
            throw new IllegalArgumentException("Reason too long (max 500 chars).");
        }
        this.reason = reason.trim();
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusChangeReason)) return false;
        StatusChangeReason that = (StatusChangeReason) o;
        return Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return reason.hashCode();
    }

    @Override
    public String toString() {
        return "StatusChangeReason{" + reason + "}";
    }
}