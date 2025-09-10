package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void shouldCreateOrderStatusWithValidStatus() {
        OrderStatus status = new OrderStatus(OrderStatus.Status.PENDING);

        assertEquals(OrderStatus.Status.PENDING, status.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderStatus(null)
        );

        assertEquals("Status cannot be null.", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameStatus() {
        OrderStatus status1 = OrderStatus.pending();
        OrderStatus status2 = OrderStatus.pending();

        assertEquals(status1, status2);
        assertEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentStatus() {
        OrderStatus pending = OrderStatus.pending();
        OrderStatus approved = OrderStatus.approved();

        assertNotEquals(pending, approved);
    }

    @Test
    void shouldNotBeEqualToNull() {
        OrderStatus status = OrderStatus.pending();

        assertNotEquals(status, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        OrderStatus status = OrderStatus.pending();
        String notOrderStatus = "PENDING";

        assertNotEquals(status, notOrderStatus);
    }

    @Test
    void shouldHaveCorrectToStringFormat() {
        OrderStatus status = OrderStatus.pending();

        assertEquals("OrderStatus{PENDING}", status.toString());
    }

    @Test
    void shouldCreateApprovedStatusWithFactory() {
        OrderStatus status = OrderStatus.approved();
        assertEquals(OrderStatus.Status.APPROVED, status.getOrderStatus());
    }

    @Test
    void shouldCreateRejectedStatusWithFactory() {
        OrderStatus status = OrderStatus.rejected();
        assertEquals(OrderStatus.Status.REJECTED, status.getOrderStatus());
    }

    @Test
    void equalsIsSymmetricAndTransitive() {
        OrderStatus a = OrderStatus.pending();
        OrderStatus b = new OrderStatus(OrderStatus.Status.PENDING);
        OrderStatus c = OrderStatus.pending();

        // simétrica
        assertTrue(a.equals(b) && b.equals(a));

        // transitiva
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));

        // reflexiva
        assertTrue(a.equals(a));
    }

    @Test
    void hashCodeConsistentWithEquals() {
        OrderStatus x = new OrderStatus(OrderStatus.Status.REJECTED);
        OrderStatus y = OrderStatus.rejected();

        assertEquals(x, y);
        assertEquals(x.hashCode(), y.hashCode());
    }
}