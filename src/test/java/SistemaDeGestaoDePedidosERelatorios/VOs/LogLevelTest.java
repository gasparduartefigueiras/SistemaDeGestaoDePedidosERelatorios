package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogLevelTest {

    @Test
    void ctorWithLevelSetsValue() {
        LogLevel l = new LogLevel(LogLevel.Level.WARN);
        assertEquals(LogLevel.Level.WARN, l.getLevel());
    }

    @Test
    void factoriesReturnExpectedLevels() {
        assertEquals(LogLevel.Level.ERROR, LogLevel.error().getLevel());
        assertEquals(LogLevel.Level.WARN,  LogLevel.warn().getLevel());
        assertEquals(LogLevel.Level.INFO,  LogLevel.info().getLevel());
    }

    @Test
    void ctorNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new LogLevel(null));
    }

    @Test
    void equalsAndHashCodeByLevel() {
        LogLevel a = new LogLevel(LogLevel.Level.ERROR);
        LogLevel b = new LogLevel(LogLevel.Level.ERROR);
        LogLevel c = new LogLevel(LogLevel.Level.INFO);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void toStringContainsLevelName() {
        String s = new LogLevel(LogLevel.Level.INFO).toString();
        assertTrue(s.contains("INFO"));
        assertTrue(s.contains("LogLevel"));
    }
}