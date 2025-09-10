package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;
import java.util.UUID;

public class LogId {

    private final UUID logId;

    public LogId() {
        this.logId = UUID.randomUUID();
    }

    public LogId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("LogId cannot be null.");
        }

        this.logId = id;
    }

    public UUID getLogId() {
        return logId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogId other = (LogId) o;
        return logId.equals(other.logId);
    }

    @Override
    public int hashCode() { return Objects.hash(logId); }

    @Override
    public String toString() { return "LogId{" + logId + "}"; }
}
