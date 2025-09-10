package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class OrderIdTest {

    @Test
    void shouldCreateOrderIdWithRandomUUID() {
        OrderId orderId = new OrderId();

        assertNotNull(orderId.getOrderId());
        assertInstanceOf(UUID.class, orderId.getOrderId());
    }

    @Test
    void shouldCreateOrderIdWithSpecificUUID() {
        UUID specificId = UUID.randomUUID();
        OrderId orderId = new OrderId(specificId);

        assertEquals(specificId, orderId.getOrderId());
    }

    @Test
    void shouldThrowExceptionWhenUUIDIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderId(null)
        );

        assertEquals("ID cannot be null.", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameUUID() {
        UUID uuid = UUID.randomUUID();
        OrderId orderId1 = new OrderId(uuid);
        OrderId orderId2 = new OrderId(uuid);

        assertEquals(orderId1, orderId2);
        assertEquals(orderId1.hashCode(), orderId2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentUUID() {
        OrderId orderId1 = new OrderId();
        OrderId orderId2 = new OrderId();

        assertNotEquals(orderId1, orderId2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        OrderId orderId = new OrderId();
        assertNotEquals(orderId, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        OrderId orderId = new OrderId();
        String notAnOrderId = "Not an OrderId";

        assertNotEquals(orderId, notAnOrderId);
    }

    @Test
    void shouldBeReflexiveSymmetricAndTransitive() {
        UUID uuid = UUID.randomUUID();
        OrderId a = new OrderId(uuid);
        OrderId b = new OrderId(uuid);
        OrderId c = new OrderId(uuid);

        // reflexivo
        assertEquals(a, a);

        // simétrico
        assertTrue(a.equals(b) && b.equals(a));

        // transitivo
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
    }

    @Test
    void shouldHaveConsistentHashCode() {
        UUID uuid = UUID.randomUUID();
        OrderId x = new OrderId(uuid);
        OrderId y = new OrderId(uuid);

        assertEquals(x.hashCode(), y.hashCode());
    }

    @Test
    void shouldHaveCorrectToStringFormat() {
        UUID uuid = UUID.randomUUID();
        OrderId orderId = new OrderId(uuid);

        String toString = orderId.toString();

        assertTrue(toString.startsWith("OrderId{"));
        assertTrue(toString.contains(uuid.toString()));
        assertTrue(toString.endsWith("}"));
    }
}