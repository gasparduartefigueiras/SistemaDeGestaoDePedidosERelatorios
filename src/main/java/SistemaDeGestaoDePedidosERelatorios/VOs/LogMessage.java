package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;

public class LogMessage {

    private final String messsage;

    public LogMessage(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("LogMessage cannot be null or empty.");
        }
        String logMessage = value.trim();
        if (logMessage.length() > 2000) {
            throw new IllegalArgumentException("LogMessage too long (max 2000 chars).");
        }
        this.messsage = logMessage;
    }

    public String getMessage() { return messsage; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogMessage)) return false;
        LogMessage that = (LogMessage) o;
        return Objects.equals(messsage, that.messsage);
    }

    @Override
    public int hashCode() { return Objects.hash(messsage); }

    @Override
    public String toString() { return "LogMessage{" + messsage + "}"; }
}