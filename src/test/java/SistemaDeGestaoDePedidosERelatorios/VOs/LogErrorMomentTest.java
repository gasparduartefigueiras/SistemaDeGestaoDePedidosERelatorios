package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class LogErrorMomentTest {

    @Test
    void defaultCtorSetsNow() {
        LocalDateTime before = LocalDateTime.now();
        LogErrorMoment m = new LogErrorMoment();
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(m.getDateTime());
        assertFalse(m.getDateTime().isBefore(before.minusSeconds(2)));
        assertFalse(m.getDateTime().isAfter(after.plusSeconds(2)));
    }

    @Test
    void explicitCtorPreservesValue() {
        LocalDateTime ts = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        LogErrorMoment m = new LogErrorMoment(ts);
        assertEquals(ts, m.getDateTime());
    }

    @Test
    void explicitCtorNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new LogErrorMoment(null));
    }

    @Test
    void equalsHashcodeByValue() {
        LocalDateTime ts = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        LogErrorMoment a = new LogErrorMoment(ts);
        LogErrorMoment b = new LogErrorMoment(ts);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toStringContainsClassAndTimestamp() {
        LocalDateTime ts = LocalDateTime.of(2023, 6, 15, 8, 30, 0);
        String s = new LogErrorMoment(ts).toString();
        assertTrue(s.contains("LogErrorMoment"));
        assertTrue(s.contains("2023-06-15T08:30"));
    }
}