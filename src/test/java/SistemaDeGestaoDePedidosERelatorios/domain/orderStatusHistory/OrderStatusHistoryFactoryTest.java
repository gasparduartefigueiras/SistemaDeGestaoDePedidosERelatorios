package SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;

class OrderStatusHistoryFactoryTest {

    private final IOrderStatusHistoryFactory factory = new OrderStatusHistoryFactory();

    @Test
    void createGeneratesIdMomentAndSetsOrderId() {
        OrderId orderId = new OrderId(UUID.randomUUID());

        LocalDateTime before = LocalDateTime.now();
        OrderStatusHistory h = factory.createHistory(
                orderId,
                OrderStatus.pending(),
                OrderStatus.approved(),
                new StatusChangeReason("ok"),
                new ChangedBy("SYSTEM")
        );
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(h.getId());
        assertNotNull(h.getId().getValue());
        assertEquals(orderId, h.getOrderId());

        assertNotNull(h.getMoment());
        LocalDateTime m = h.getMoment().getDateTime();
        assertFalse(m.isBefore(before.minusSeconds(2)));
        assertFalse(m.isAfter(after.plusSeconds(2)));
    }

    @Test
    void createAssignsAllValues() {
        OrderId orderId = new OrderId(UUID.randomUUID());
        OrderStatus initial = OrderStatus.rejected();
        OrderStatus fin = OrderStatus.approved();
        StatusChangeReason reason = new StatusChangeReason("manual override");
        ChangedBy by = new ChangedBy("ADMIN");

        OrderStatusHistory h = factory.createHistory(orderId, initial, fin, reason, by);

        assertEquals(orderId, h.getOrderId());
        assertEquals(initial, h.getInitialStatus());
        assertEquals(fin, h.getFinalStatus());
        assertEquals(reason, h.getReason());
        assertEquals(by, h.getChangedBy());
        assertNotNull(h.getId());
        assertNotNull(h.getMoment());
    }

    @Test
    void createThrowsWhenInitialEqualsFinal() {
        OrderId orderId = new OrderId(UUID.randomUUID());
        assertThrows(IllegalArgumentException.class, () ->
                factory.createHistory(
                        orderId,
                        OrderStatus.approved(),
                        OrderStatus.approved(),
                        null,
                        new ChangedBy("SYSTEM")
                )
        );
    }

    @Test
    void createThrowsWhenNullArguments() {
        OrderId orderId = new OrderId(UUID.randomUUID());

        // orderId null
        assertThrows(IllegalArgumentException.class, () ->
                factory.createHistory(
                        null,
                        OrderStatus.pending(),
                        OrderStatus.approved(),
                        null,
                        new ChangedBy("SYSTEM"))
        );

        // initial null
        assertThrows(IllegalArgumentException.class, () ->
                factory.createHistory(
                        orderId,
                        null,
                        OrderStatus.approved(),
                        null,
                        new ChangedBy("SYSTEM"))
        );

        // final null
        assertThrows(IllegalArgumentException.class, () ->
                factory.createHistory(
                        orderId,
                        OrderStatus.pending(),
                        null,
                        null,
                        new ChangedBy("SYSTEM"))
        );

        // changedBy null
        assertThrows(IllegalArgumentException.class, () ->
                factory.createHistory(
                        orderId,
                        OrderStatus.pending(),
                        OrderStatus.approved(),
                        null,
                        null)
        );
    }

    @Test
    void recreateUsesProvidedIdOrderAndMoment() {
        OrderStatusHistoryId id = new OrderStatusHistoryId(UUID.randomUUID());
        OrderId orderId = new OrderId(UUID.randomUUID());
        StatusChangedMoment moment = new StatusChangedMoment(LocalDateTime.of(2025, 1, 1, 10, 0));

        OrderStatusHistory h = factory.recreateHistory(
                id,
                orderId,
                OrderStatus.pending(),
                OrderStatus.rejected(),
                new StatusChangeReason("qa failed"),
                new ChangedBy("ADMIN"),
                moment
        );

        assertEquals(id, h.getId());
        assertEquals(orderId, h.getOrderId());
        assertEquals(moment, h.getMoment());
        assertEquals(OrderStatus.Status.PENDING, h.getInitialStatus().getOrderStatus());
        assertEquals(OrderStatus.Status.REJECTED, h.getFinalStatus().getOrderStatus());
    }
}