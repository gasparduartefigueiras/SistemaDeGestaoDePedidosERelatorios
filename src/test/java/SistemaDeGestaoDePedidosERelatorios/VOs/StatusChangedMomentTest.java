package SistemaDeGestaoDePedidosERelatorios.VOs;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class StatusChangedMomentTest {

    @Test
    void defaultCtor_setsNow() {
        LocalDateTime before = LocalDateTime.now();
        StatusChangedMoment sca = new StatusChangedMoment();
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(sca.getDateTime());
        // tolerância de 2 segundos
        assertFalse(sca.getDateTime().isBefore(before.minusSeconds(2)));
        assertFalse(sca.getDateTime().isAfter(after.plusSeconds(2)));
    }

    @Test
    void explicitCtor_preservesValue() {
        LocalDateTime ts = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        StatusChangedMoment sca = new StatusChangedMoment(ts);
        assertEquals(ts, sca.getDateTime());
    }

    @Test
    void explicitCtor_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> new StatusChangedMoment(null));
    }

    @Test
    void equality_and_hashcode_by_value() {
        LocalDateTime ts = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        StatusChangedMoment a = new StatusChangedMoment(ts);
        StatusChangedMoment b = new StatusChangedMoment(ts);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toString_contains_value() {
        LocalDateTime ts = LocalDateTime.of(2023, 6, 15, 8, 30, 0);
        String s = new StatusChangedMoment(ts).toString();
        assertTrue(s.contains("StatusChangedAt"));
        assertTrue(s.contains("2023-06-15T08:30"));
    }
}