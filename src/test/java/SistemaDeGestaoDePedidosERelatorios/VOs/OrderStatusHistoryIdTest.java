package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class OrderStatusHistoryIdTest {

    @Test
    void defaultConstructorGeneratesUuid() {
        OrderStatusHistoryId id = new OrderStatusHistoryId();
        assertNotNull(id.getValue());
    }

    @Test
    void explicitConstructorPreservesUuid() {
        UUID uuid = UUID.randomUUID();
        OrderStatusHistoryId id = new OrderStatusHistoryId(uuid);
        assertEquals(uuid, id.getValue());
    }

    @Test
    void explicitConstructorThrowsIfNull() {
        assertThrows(IllegalArgumentException.class, () -> new OrderStatusHistoryId(null));
    }

    @Test
    void equalsAndHashCodeByValue() {
        UUID uuid = UUID.randomUUID();
        OrderStatusHistoryId a = new OrderStatusHistoryId(uuid);
        OrderStatusHistoryId b = new OrderStatusHistoryId(uuid);
        OrderStatusHistoryId c = new OrderStatusHistoryId(UUID.randomUUID());

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void toStringContainsClassAndUuid() {
        UUID uuid = UUID.randomUUID();
        String s = new OrderStatusHistoryId(uuid).toString();
        assertTrue(s.contains("OrderStatusHistoryId"));
        assertTrue(s.contains(uuid.toString()));
    }
}