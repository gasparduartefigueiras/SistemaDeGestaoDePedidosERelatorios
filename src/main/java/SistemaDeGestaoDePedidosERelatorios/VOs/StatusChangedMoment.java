package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.time.LocalDateTime;
import java.util.Objects;

public class StatusChangedMoment {

    private final LocalDateTime dateTime;

    public StatusChangedMoment() {
        this.dateTime = LocalDateTime.now();
    }

    public StatusChangedMoment(LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("StatusChangedAt cannot be null.");
        }
        this.dateTime = value;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusChangedMoment)) return false;
        StatusChangedMoment that = (StatusChangedMoment) o;
        return Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() { return Objects.hash(dateTime); }

    @Override
    public String toString() { return "StatusChangedAt{" + dateTime + "}"; }
}