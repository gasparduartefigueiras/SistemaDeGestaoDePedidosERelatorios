package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.time.LocalDateTime;
import java.util.Objects;

public class LogErrorMoment {

    private final LocalDateTime dateTime;

    public LogErrorMoment() {
        this.dateTime = LocalDateTime.now();
    }

    public LogErrorMoment(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("LogErrorMoment cannot be null.");
        }
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogErrorMoment)) return false;
        LogErrorMoment that = (LogErrorMoment) o;
        return Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime);
    }

    @Override
    public String toString() {
        return "LogErrorMoment{" + dateTime + "}";
    }
}