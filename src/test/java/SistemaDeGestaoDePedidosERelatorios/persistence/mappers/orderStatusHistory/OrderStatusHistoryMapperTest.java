package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.orderStatusHistory;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.IOrderStatusHistoryFactory;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.orderStatusHistory.OrderStatusHistoryDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderStatusHistoryMapperTest {

    private IOrderStatusHistoryFactory factory;
    private OrderStatusHistoryMapper mapper;

    @BeforeEach
    void setup() {
        factory = mock(IOrderStatusHistoryFactory.class);
        mapper = new OrderStatusHistoryMapper(factory);
    }

    @Test
    void constructorRequiresFactory() {
        assertThrows(NullPointerException.class, () -> new OrderStatusHistoryMapper(null));
    }

    @Test
    void toDataModelHappyPath() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderStatus.Status initialEnum = OrderStatus.Status.PENDING;
        OrderStatus.Status finalEnum = OrderStatus.Status.APPROVED;
        String reason = "ok";
        String changedBy = "system";
        LocalDateTime when = LocalDateTime.of(2025, 1, 1, 12, 0);

        OrderStatusHistory entity = mock(OrderStatusHistory.class);
        OrderStatusHistoryId oshId = mock(OrderStatusHistoryId.class);
        OrderId oId = mock(OrderId.class);
        OrderStatus initial = mock(OrderStatus.class);
        OrderStatus fin = mock(OrderStatus.class);
        StatusChangeReason r = mock(StatusChangeReason.class);
        ChangedBy cb = mock(ChangedBy.class);
        StatusChangedMoment moment = mock(StatusChangedMoment.class);

        when(entity.getId()).thenReturn(oshId);
        when(entity.getOrderId()).thenReturn(oId);
        when(entity.getInitialStatus()).thenReturn(initial);
        when(entity.getFinalStatus()).thenReturn(fin);
        when(entity.getReason()).thenReturn(r);
        when(entity.getChangedBy()).thenReturn(cb);
        when(entity.getMoment()).thenReturn(moment);

        when(oshId.getValue()).thenReturn(id);
        when(oId.getOrderId()).thenReturn(orderId);
        when(initial.getOrderStatus()).thenReturn(initialEnum);
        when(fin.getOrderStatus()).thenReturn(finalEnum);
        when(r.getReason()).thenReturn(reason);
        when(cb.getValue()).thenReturn(changedBy);
        when(moment.getDateTime()).thenReturn(when);

        // Act
        OrderStatusHistoryDataModel dm = mapper.toDataModel(entity);

        // Assert (apenas checks centrais)
        assertEquals(id, dm.getId());
        assertEquals(orderId, dm.getOrderId());
        assertEquals(initialEnum.name(), dm.getInitialStatus());
        assertEquals(finalEnum.name(), dm.getFinalStatus());
        assertEquals(reason, dm.getReason());
        assertEquals(changedBy, dm.getChangedBy());
        assertEquals(when, dm.getChangedAt());

        verifyNoInteractions(factory);
    }

    @Test
    void toDataModelThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> mapper.toDataModel(null));
        verifyNoInteractions(factory);
    }

    @Test
    void toDomainHappyPath() {
        // Arrange (usar DataModel real)
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderStatusHistoryDataModel dm = new OrderStatusHistoryDataModel(
                id, orderId, "PENDING", "REJECTED", "motivo", "admin",
                LocalDateTime.of(2025, 2, 2, 9, 30)
        );

        OrderStatusHistory expected = mock(OrderStatusHistory.class);
        when(factory.recreateHistory(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(expected);

        // Act
        OrderStatusHistory result = mapper.toDomain(dm);

        // Assert
        assertSame(expected, result);
        verify(factory, times(1)).recreateHistory(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void toDomainHandlesNullReason() {
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderStatusHistoryDataModel dm = new OrderStatusHistoryDataModel(
                id, orderId, "PENDING", "APPROVED", null, "system",
                LocalDateTime.of(2025, 3, 3, 8, 0)
        );

        when(factory.recreateHistory(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(OrderStatusHistory.class));

        // Apenas garantir que não lança e invoca a factory
        mapper.toDomain(dm);
        verify(factory, times(1)).recreateHistory(any(), any(), any(), any(), isNull(), any(), any());
    }

    @Test
    void toDomainThrowsIllegalArgumentOnNull() {
        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(null));
        verifyNoInteractions(factory);
    }

    @Test
    void toDomainWrapsFactoryExceptions() {
        OrderStatusHistoryDataModel dm = new OrderStatusHistoryDataModel(
                UUID.randomUUID(), UUID.randomUUID(), "PENDING", "APPROVED", "ok", "user", LocalDateTime.now()
        );

        when(factory.recreateHistory(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new IllegalStateException("falha"));

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(dm));
        assertTrue(ex.getMessage().contains("OrderStatusHistoryDataModel"));
        assertTrue(ex.getCause() instanceof IllegalStateException);
    }

    @Test
    void toDataModelHandlesNullReason() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        LocalDateTime when = LocalDateTime.of(2025, 6, 1, 10, 0);

        OrderStatusHistory entity = mock(OrderStatusHistory.class);
        OrderStatusHistoryId oshId = mock(OrderStatusHistoryId.class);
        OrderId oId = mock(OrderId.class);
        OrderStatus initial = mock(OrderStatus.class);
        OrderStatus fin = mock(OrderStatus.class);
        ChangedBy cb = mock(ChangedBy.class);
        StatusChangedMoment moment = mock(StatusChangedMoment.class);

        when(entity.getId()).thenReturn(oshId);
        when(entity.getOrderId()).thenReturn(oId);
        when(entity.getInitialStatus()).thenReturn(initial);
        when(entity.getFinalStatus()).thenReturn(fin);
        when(entity.getReason()).thenReturn(null); // <-- caso a testar
        when(entity.getChangedBy()).thenReturn(cb);
        when(entity.getMoment()).thenReturn(moment);

        when(oshId.getValue()).thenReturn(id);
        when(oId.getOrderId()).thenReturn(orderId);
        when(initial.getOrderStatus()).thenReturn(OrderStatus.Status.PENDING);
        when(fin.getOrderStatus()).thenReturn(OrderStatus.Status.APPROVED);
        when(cb.getValue()).thenReturn("system");
        when(moment.getDateTime()).thenReturn(when);

        // Act
        OrderStatusHistoryDataModel dm = mapper.toDataModel(entity);

        // Assert
        assertEquals(id, dm.getId());
        assertEquals(orderId, dm.getOrderId());
        assertEquals("PENDING", dm.getInitialStatus());
        assertEquals("APPROVED", dm.getFinalStatus());
        assertNull(dm.getReason()); // <-- verificação principal
        assertEquals("system", dm.getChangedBy());
        assertEquals(when, dm.getChangedAt());

        verifyNoInteractions(factory);
    }
}