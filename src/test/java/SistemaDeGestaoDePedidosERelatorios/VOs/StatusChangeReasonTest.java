package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

class StatusChangeReasonTest {

    @Test
    void shouldCreateValidReason() {
        StatusChangeReason reason = new StatusChangeReason("Order approved by admin");
        assertEquals("Order approved by admin", reason.getReason());
    }

    @Test
    void shouldTrimWhitespace() {
        StatusChangeReason reason = new StatusChangeReason("   Delayed due to payment   ");
        assertEquals("Delayed due to payment", reason.getReason());
    }

    @Test
    void shouldThrowIfReasonIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new StatusChangeReason(null));
    }

    @Test
    void shouldThrowIfReasonIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new StatusChangeReason("   "));
    }

    @Test
    void shouldThrowIfReasonIsTooLong() {
        String tooLong = String.join("", Collections.nCopies(501, "a"));
        assertThrows(IllegalArgumentException.class, () -> new StatusChangeReason(tooLong));
    }

    @Test
    void shouldBeEqualIfSameValue() {
        StatusChangeReason r1 = new StatusChangeReason("Changed by system");
        StatusChangeReason r2 = new StatusChangeReason("Changed by system");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void toStringShouldContainReason() {
        StatusChangeReason reason = new StatusChangeReason("Rejected by system");
        String result = reason.toString();
        assertTrue(result.contains("Rejected by system"));
        assertTrue(result.contains("StatusChangeReason"));
    }
}