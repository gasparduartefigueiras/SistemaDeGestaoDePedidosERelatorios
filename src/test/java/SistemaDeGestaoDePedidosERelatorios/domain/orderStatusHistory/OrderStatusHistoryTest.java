package SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import java.time.LocalDateTime;
import java.util.UUID;

class OrderStatusHistoryTest {

    private OrderStatusHistoryId newHistoryId() {
        return new OrderStatusHistoryId(UUID.randomUUID());
    }

    private OrderId newOrderId() {
        return new OrderId(UUID.randomUUID());
    }

    @Test
    void shouldCreateValidHistory() {
        OrderStatus initial = OrderStatus.pending();
        OrderStatus finalStatus = OrderStatus.approved();
        StatusChangeReason reason = new StatusChangeReason("Manual approval");
        ChangedBy changedBy = new ChangedBy("SYSTEM");
        StatusChangedMoment moment = new StatusChangedMoment(LocalDateTime.now());

        OrderStatusHistory history = new OrderStatusHistory(
                newHistoryId(), newOrderId(),
                initial, finalStatus, reason, changedBy, moment
        );

        assertEquals(initial, history.getInitialStatus());
        assertEquals(finalStatus, history.getFinalStatus());
        assertEquals(reason, history.getReason());
        assertEquals(changedBy, history.getChangedBy());
        assertEquals(moment, history.getMoment());
        assertNotNull(history.getId());
        assertNotNull(history.getOrderId());
    }

    @Test
    void shouldThrowIfHistoryIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new OrderStatusHistory(
                        null,
                        newOrderId(),
                        OrderStatus.pending(),
                        OrderStatus.approved(),
                        null,
                        new ChangedBy("SYSTEM"),
                        new StatusChangedMoment(LocalDateTime.now()))
        );
    }

    @Test
    void shouldThrowIfOrderIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new OrderStatusHistory(
                        newHistoryId(),
                        null,
                        OrderStatus.pending(),
                        OrderStatus.approved(),
                        null,
                        new ChangedBy("SYSTEM"),
                        new StatusChangedMoment(LocalDateTime.now()))
        );
    }

    @Test
    void shouldThrowIfInitialIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new OrderStatusHistory(
                        newHistoryId(), newOrderId(),
                        null,
                        OrderStatus.approved(),
                        null,
                        new ChangedBy("SYSTEM"),
                        new StatusChangedMoment(LocalDateTime.now()))
        );
    }

    @Test
    void shouldThrowIfFinalIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new OrderStatusHistory(
                        newHistoryId(), newOrderId(),
                        OrderStatus.pending(),
                        null,
                        null,
                        new ChangedBy("SYSTEM"),
                        new StatusChangedMoment(LocalDateTime.now()))
        );
    }

    @Test
    void shouldThrowIfChangedByIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new OrderStatusHistory(
                        newHistoryId(), newOrderId(),
                        OrderStatus.pending(),
                        OrderStatus.rejected(),
                        null,
                        null,
                        new StatusChangedMoment(LocalDateTime.now()))
        );
    }

    @Test
    void shouldThrowIfMomentIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new OrderStatusHistory(
                        newHistoryId(), newOrderId(),
                        OrderStatus.pending(),
                        OrderStatus.rejected(),
                        null,
                        new ChangedBy("SYSTEM"),
                        null)
        );
    }

    @Test
    void shouldThrowIfInitialEqualsFinal() {
        assertThrows(IllegalArgumentException.class, () ->
                new OrderStatusHistory(
                        newHistoryId(), newOrderId(),
                        OrderStatus.approved(),
                        OrderStatus.approved(),
                        null,
                        new ChangedBy("SYSTEM"),
                        new StatusChangedMoment(LocalDateTime.now()))
        );
    }

    @Test
    void equalsAndHashCodeUseId() {
        OrderStatusHistoryId id = newHistoryId();
        OrderId orderId = newOrderId();

        OrderStatusHistory h1 = new OrderStatusHistory(
                id, orderId,
                OrderStatus.pending(),
                OrderStatus.rejected(),
                new StatusChangeReason("Invalid data"),
                new ChangedBy("ADMIN"),
                new StatusChangedMoment(LocalDateTime.now())
        );
        OrderStatusHistory h2 = new OrderStatusHistory(
                id, orderId,
                OrderStatus.pending(),
                OrderStatus.rejected(),
                new StatusChangeReason("Invalid data"),
                new ChangedBy("ADMIN"),
                new StatusChangedMoment(LocalDateTime.now())
        );

        assertEquals(h1, h2);
        assertEquals(h1.hashCode(), h2.hashCode());
    }

    @Test
    void shouldAllowNullReason() {
        OrderStatusHistory h = new OrderStatusHistory(
                newHistoryId(), newOrderId(),
                OrderStatus.pending(),
                OrderStatus.approved(),
                null,
                new ChangedBy("SYSTEM"),
                new StatusChangedMoment(LocalDateTime.now())
        );
        assertNull(h.getReason());
    }

    @Test
    void approvedToRejectedIsValid() {
        OrderStatusHistory h = new OrderStatusHistory(
                newHistoryId(), newOrderId(),
                OrderStatus.approved(),
                OrderStatus.rejected(),
                new StatusChangeReason("QA failed"),
                new ChangedBy("ADMIN"),
                new StatusChangedMoment(LocalDateTime.now())
        );
        assertEquals(OrderStatus.rejected(), h.getFinalStatus());
    }

    @Test
    void rejectedToApprovedIsValid() {
        OrderStatusHistory h = new OrderStatusHistory(
                newHistoryId(), newOrderId(),
                OrderStatus.rejected(),
                OrderStatus.approved(),
                new StatusChangeReason("Manual override"),
                new ChangedBy("ADMIN"),
                new StatusChangedMoment(LocalDateTime.now())
        );
        assertEquals(OrderStatus.approved(), h.getFinalStatus());
    }

    @Test
    void toStringContainsKeyParts() {
        OrderStatusHistory h = new OrderStatusHistory(
                newHistoryId(), newOrderId(),
                OrderStatus.pending(),
                OrderStatus.approved(),
                new StatusChangeReason("ok"),
                new ChangedBy("SYSTEM"),
                new StatusChangedMoment(LocalDateTime.of(2025, 1, 1, 10, 0))
        );
        String s = h.toString();
        assertTrue(s.contains("id"));
        assertTrue(s.contains("orderId"));
        assertTrue(s.contains("initialStatus"));
        assertTrue(s.contains("finalStatus"));
        assertTrue(s.contains("changedBy"));
    }
}