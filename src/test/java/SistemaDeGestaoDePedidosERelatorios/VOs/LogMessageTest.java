package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogMessageTest {

    @Test
    void shouldCreateValidMessage() {
        LogMessage msg = new LogMessage("System failure");
        assertEquals("System failure", msg.getMessage());
    }

    @Test
    void shouldTrimWhitespace() {
        LogMessage msg = new LogMessage("   Error occurred   ");
        assertEquals("Error occurred", msg.getMessage());
    }

    @Test
    void shouldThrowIfNull() {
        assertThrows(IllegalArgumentException.class, () -> new LogMessage(null));
    }

    @Test
    void shouldThrowIfEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new LogMessage("   "));
    }

    @Test
    void shouldThrowIfTooLong() {
        String longMessage = String.join("", java.util.Collections.nCopies(2001, "a"));
        assertThrows(IllegalArgumentException.class, () -> new LogMessage(longMessage));
    }

    @Test
    void equalsAndHashCodeByValue() {
        LogMessage a = new LogMessage("Database down");
        LogMessage b = new LogMessage("Database down");
        LogMessage c = new LogMessage("Another issue");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void toStringContainsMessage() {
        LogMessage msg = new LogMessage("Disk full");
        String s = msg.toString();
        assertTrue(s.contains("Disk full"));
        assertTrue(s.contains("LogMessage"));
    }
}