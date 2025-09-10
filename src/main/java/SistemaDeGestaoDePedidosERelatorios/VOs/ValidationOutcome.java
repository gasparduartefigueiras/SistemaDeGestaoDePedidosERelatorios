package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;

public class ValidationOutcome {

    private final boolean approved;
    private final String message;

    public ValidationOutcome(boolean approved, String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Validation message cannot be null or empty");
        }
        this.approved = approved;
        this.message = message;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationOutcome)) return false;
        ValidationOutcome that = (ValidationOutcome) o;
        return approved == that.approved &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(approved, message);
    }

    @Override
    public String toString() {
        return "ValidationOutcome{" +
                "approved=" + approved +
                ", message='" + message + '\'' +
                '}';
    }
}