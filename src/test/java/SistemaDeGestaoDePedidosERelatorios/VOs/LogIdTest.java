package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class LogIdTest {

    @Test
    void defaultCtorGeneratesUuid() {
        LogId id = new LogId();
        assertNotNull(id.getLogId());
    }

    @Test
    void explicitCtorPreservesUuid() {
        UUID uuid = UUID.randomUUID();
        LogId id = new LogId(uuid);
        assertEquals(uuid, id.getLogId());
    }

    @Test
    void explicitCtorNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new LogId(null));
    }

    @Test
    void equalsAndHashCodeByValue() {
        UUID uuid = UUID.randomUUID();
        LogId a = new LogId(uuid);
        LogId b = new LogId(uuid);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentUuidsNotEqual() {
        LogId a = new LogId(UUID.randomUUID());
        LogId b = new LogId(UUID.randomUUID());
        assertNotEquals(a, b);
    }

    @Test
    void toStringContainsUuid() {
        UUID uuid = UUID.randomUUID();
        String s = new LogId(uuid).toString();
        assertTrue(s.contains(uuid.toString()));
        assertTrue(s.contains("LogId"));
    }
}