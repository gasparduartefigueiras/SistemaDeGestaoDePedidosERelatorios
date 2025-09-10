package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;

public class ValidationMessage {
    private final String validationMessage;

    public ValidationMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Validation message cannot be null or empty.");
        }

        this.validationMessage = message.trim();
    }

    public static ValidationMessage success(String msg) {
        if (msg == null) {
            return new ValidationMessage("SUCCESS");
        } else {
            return new ValidationMessage(msg);
        }
    }

    public static ValidationMessage error(String errorMessage) {
        return new ValidationMessage("ERROR: " + errorMessage);
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationMessage that = (ValidationMessage) o;
        return Objects.equals(validationMessage, that.validationMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationMessage);
    }

    @Override
    public String toString() {
        return "ValidationMessage{" + validationMessage + "}";
    }
}