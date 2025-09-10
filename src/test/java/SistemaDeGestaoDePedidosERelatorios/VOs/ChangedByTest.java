package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChangedByTest {

    @Test
    void shouldCreateValidChangedBy() {
        ChangedBy cb = new ChangedBy("SYSTEM");
        assertEquals("SYSTEM", cb.getValue());
    }

    @Test
    void shouldTrimWhitespace() {
        ChangedBy cb = new ChangedBy("   admin   ");
        assertEquals("admin", cb.getValue());
    }

    @Test
    void shouldThrowIfNull() {
        assertThrows(IllegalArgumentException.class, () -> new ChangedBy(null));
    }

    @Test
    void shouldThrowIfEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new ChangedBy("   "));
    }

    @Test
    void shouldThrowIfTooLong() {
        String tooLong = String.join("", java.util.Collections.nCopies(21, "a")); // 21 chars
        assertThrows(IllegalArgumentException.class, () -> new ChangedBy(tooLong));
    }

    @Test
    void shouldBeEqualIfSameValue() {
        ChangedBy cb1 = new ChangedBy("USER1");
        ChangedBy cb2 = new ChangedBy("USER1");
        assertEquals(cb1, cb2);
        assertEquals(cb1.hashCode(), cb2.hashCode());
    }

    @Test
    void toStringShouldContainValue() {
        ChangedBy cb = new ChangedBy("SYSTEM");
        String result = cb.toString();
        assertTrue(result.contains("SYSTEM"));
        assertTrue(result.contains("ChangedBy"));
    }
}