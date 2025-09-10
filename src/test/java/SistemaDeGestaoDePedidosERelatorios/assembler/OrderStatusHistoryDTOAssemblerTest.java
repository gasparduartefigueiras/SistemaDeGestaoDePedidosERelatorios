package SistemaDeGestaoDePedidosERelatorios.assembler;

import SistemaDeGestaoDePedidosERelatorios.DTOs.orderStatusHistory.OrderStatusHistoryResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistory;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderStatusHistoryDTOAssemblerTest {

    @Test
    void toResponseDTOHappyPath() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderStatus.Status initial = OrderStatus.Status.PENDING;
        OrderStatus.Status fin = OrderStatus.Status.APPROVED;
        String reason = "ok";
        String changedBy = "admin";
        LocalDateTime when = LocalDateTime.of(2025, 9, 10, 16, 45);
        Instant expectedInstant = when.toInstant(ZoneOffset.UTC);

        OrderStatusHistory entity = mock(OrderStatusHistory.class);
        OrderStatusHistoryId oshId = mock(OrderStatusHistoryId.class);
        OrderId oId = mock(OrderId.class);
        OrderStatus initialStatus = mock(OrderStatus.class);
        OrderStatus finalStatus = mock(OrderStatus.class);
        StatusChangeReason r = mock(StatusChangeReason.class);
        ChangedBy cb = mock(ChangedBy.class);
        StatusChangedMoment moment = mock(StatusChangedMoment.class);

        when(entity.getId()).thenReturn(oshId);
        when(oshId.getValue()).thenReturn(id);

        when(entity.getOrderId()).thenReturn(oId);
        when(oId.getOrderId()).thenReturn(orderId);

        when(entity.getInitialStatus()).thenReturn(initialStatus);
        when(initialStatus.getOrderStatus()).thenReturn(initial);

        when(entity.getFinalStatus()).thenReturn(finalStatus);
        when(finalStatus.getOrderStatus()).thenReturn(fin);

        when(entity.getReason()).thenReturn(r);
        when(r.getReason()).thenReturn(reason);

        when(entity.getChangedBy()).thenReturn(cb);
        when(cb.getValue()).thenReturn(changedBy);

        when(entity.getMoment()).thenReturn(moment);
        when(moment.getDateTime()).thenReturn(when);

        // Act
        OrderStatusHistoryResponseDTO dto = OrderStatusHistoryDTOAssembler.toResponseDTO(entity);

        // Assert
        assertEquals(id.toString(), dto.getId());
        assertEquals(orderId.toString(), dto.getOrderId());
        assertEquals("PENDING", dto.getInitialStatus());
        assertEquals("APPROVED", dto.getFinalStatus());
        assertEquals("ok", dto.getReason());
        assertEquals("admin", dto.getChangedBy());
        assertEquals(expectedInstant, dto.getChangedAt());
    }

    @Test
    void toResponseDTONullReasonYieldsNullReason() {
        // Arrange
        OrderStatusHistory entity = mock(OrderStatusHistory.class);
        OrderStatusHistoryId oshId = mock(OrderStatusHistoryId.class);
        OrderId oId = mock(OrderId.class);
        OrderStatus initialStatus = mock(OrderStatus.class);
        OrderStatus finalStatus = mock(OrderStatus.class);
        ChangedBy cb = mock(ChangedBy.class);
        StatusChangedMoment moment = mock(StatusChangedMoment.class);

        when(entity.getId()).thenReturn(oshId);
        when(oshId.getValue()).thenReturn(UUID.randomUUID());

        when(entity.getOrderId()).thenReturn(oId);
        when(oId.getOrderId()).thenReturn(UUID.randomUUID());

        when(entity.getInitialStatus()).thenReturn(initialStatus);
        when(initialStatus.getOrderStatus()).thenReturn(OrderStatus.Status.PENDING);

        when(entity.getFinalStatus()).thenReturn(finalStatus);
        when(finalStatus.getOrderStatus()).thenReturn(OrderStatus.Status.REJECTED);

        when(entity.getReason()).thenReturn(null); // <- importante

        when(entity.getChangedBy()).thenReturn(cb);
        when(cb.getValue()).thenReturn("system");

        when(entity.getMoment()).thenReturn(moment);
        when(moment.getDateTime()).thenReturn(LocalDateTime.now());

        // Act
        OrderStatusHistoryResponseDTO dto = OrderStatusHistoryDTOAssembler.toResponseDTO(entity);

        // Assert
        assertNull(dto.getReason());
        assertEquals("system", dto.getChangedBy());
    }

    @Test
    void toResponseDTONullIdsYieldNullStrings() {
        // Arrange
        OrderStatusHistory entity = mock(OrderStatusHistory.class);

        when(entity.getId()).thenReturn(null);
        when(entity.getOrderId()).thenReturn(null);
        when(entity.getInitialStatus()).thenReturn(null);
        when(entity.getFinalStatus()).thenReturn(null);
        when(entity.getReason()).thenReturn(null);
        when(entity.getChangedBy()).thenReturn(null);
        when(entity.getMoment()).thenReturn(null);

        // Act
        OrderStatusHistoryResponseDTO dto = OrderStatusHistoryDTOAssembler.toResponseDTO(entity);

        // Assert
        assertNull(dto.getId());
        assertNull(dto.getOrderId());
        assertNull(dto.getInitialStatus());
        assertNull(dto.getFinalStatus());
        assertNull(dto.getReason());
        assertNull(dto.getChangedBy());
        assertNull(dto.getChangedAt());
    }

    @Test
    void toResponseDTONullChangedByYieldsNullChangedBy() {
        // Arrange
        OrderStatusHistory entity = mock(OrderStatusHistory.class);
        OrderStatusHistoryId oshId = mock(OrderStatusHistoryId.class);
        OrderId oId = mock(OrderId.class);
        OrderStatus initialStatus = mock(OrderStatus.class);
        OrderStatus finalStatus = mock(OrderStatus.class);
        StatusChangedMoment moment = mock(StatusChangedMoment.class);

        when(entity.getId()).thenReturn(oshId);
        when(oshId.getValue()).thenReturn(UUID.randomUUID());

        when(entity.getOrderId()).thenReturn(oId);
        when(oId.getOrderId()).thenReturn(UUID.randomUUID());

        when(entity.getInitialStatus()).thenReturn(initialStatus);
        when(initialStatus.getOrderStatus()).thenReturn(OrderStatus.Status.PENDING);

        when(entity.getFinalStatus()).thenReturn(finalStatus);
        when(finalStatus.getOrderStatus()).thenReturn(OrderStatus.Status.APPROVED);

        when(entity.getReason()).thenReturn(new StatusChangeReason("ok"));

        when(entity.getChangedBy()).thenReturn(null); // <- importante

        when(entity.getMoment()).thenReturn(moment);
        when(moment.getDateTime()).thenReturn(LocalDateTime.now());

        // Act
        OrderStatusHistoryResponseDTO dto = OrderStatusHistoryDTOAssembler.toResponseDTO(entity);

        // Assert
        assertNull(dto.getChangedBy());
        assertEquals("PENDING", dto.getInitialStatus());
        assertEquals("APPROVED", dto.getFinalStatus());
    }

    @Test
    void toResponseDTONullMomentYieldsNullInstant() {
        // Arrange
        OrderStatusHistory entity = mock(OrderStatusHistory.class);
        OrderStatusHistoryId oshId = mock(OrderStatusHistoryId.class);
        OrderId oId = mock(OrderId.class);
        OrderStatus initialStatus = mock(OrderStatus.class);
        OrderStatus finalStatus = mock(OrderStatus.class);

        when(entity.getId()).thenReturn(oshId);
        when(oshId.getValue()).thenReturn(UUID.randomUUID());

        when(entity.getOrderId()).thenReturn(oId);
        when(oId.getOrderId()).thenReturn(UUID.randomUUID());

        when(entity.getInitialStatus()).thenReturn(initialStatus);
        when(initialStatus.getOrderStatus()).thenReturn(OrderStatus.Status.PENDING);

        when(entity.getFinalStatus()).thenReturn(finalStatus);
        when(finalStatus.getOrderStatus()).thenReturn(OrderStatus.Status.REJECTED);

        when(entity.getReason()).thenReturn(new StatusChangeReason("bad"));
        when(entity.getChangedBy()).thenReturn(new ChangedBy("user"));

        when(entity.getMoment()).thenReturn(null); // <- momento nulo

        // Act
        OrderStatusHistoryResponseDTO dto = OrderStatusHistoryDTOAssembler.toResponseDTO(entity);

        // Assert
        assertNull(dto.getChangedAt());
    }
}