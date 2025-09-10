package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.time.LocalDate;
import java.util.Objects;

public class CreationDate {

    private final LocalDate creationDate;

    public CreationDate() {
        this.creationDate = LocalDate.now();
    }

    public CreationDate (LocalDate dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime cannot be null.");
        }

        this.creationDate = dateTime;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreationDate that = (CreationDate) o;
        return Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creationDate);
    }

    @Override
    public String toString() {
        return "CreationDate{" + creationDate + "}";
    }
}
