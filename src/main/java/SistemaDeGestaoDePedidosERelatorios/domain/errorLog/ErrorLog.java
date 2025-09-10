package SistemaDeGestaoDePedidosERelatorios.domain.errorLog;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;

import java.util.Objects;

public class ErrorLog {

    private final LogId id;
    private final LogLevel level;
    private final LogMessage message;
    private final LogErrorMoment date;
    private final OrderId orderId;

    public ErrorLog(LogId id, LogLevel level, LogMessage message, LogErrorMoment dateTime, OrderId orderId) {
        if (id == null) {
            throw new IllegalArgumentException("LogId cannot be null");
        }
        if (level == null) {
            throw new IllegalArgumentException("LogLevel cannot be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("LogMessage cannot be null");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("LogErrorMoment cannot be null");
        }

        this.id = id;
        this.level = level;
        this.message = message;
        this.date = dateTime;
        this.orderId = orderId;
    }
        public LogId getId() {
            return id;
    }
        public LogLevel getLevel() {
        return level;
    }
        public LogMessage getMessage() {
        return message;
    }
        public LogErrorMoment getErrorMoment() {
        return date;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorLog)) return false;
        ErrorLog that = (ErrorLog) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ErrorLog{id=" + id + ", level=" + level + ", message=" + message + ", errorMoment=" + date +  ", orderId=" + orderId + "}";
    }
}
