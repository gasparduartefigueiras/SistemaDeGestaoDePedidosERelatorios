package SistemaDeGestaoDePedidosERelatorios.DTOs.orderStatusHistory;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusHistoryResponseDTOTest {

    @Test
    void constructorAndGettersShouldReturnCorrectValues() {
        // Arrange
        String id = "123";
        String orderId = "456";
        String initialStatus = "PENDING";
        String finalStatus = "APPROVED";
        String reason = "Validation passed";
        String changedBy = "system";
        Instant changedAt = Instant.now();

        // Act
        OrderStatusHistoryResponseDTO dto = new OrderStatusHistoryResponseDTO(
                id, orderId, initialStatus, finalStatus, reason, changedBy, changedAt
        );

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(orderId, dto.getOrderId());
        assertEquals(initialStatus, dto.getInitialStatus());
        assertEquals(finalStatus, dto.getFinalStatus());
        assertEquals(reason, dto.getReason());
        assertEquals(changedBy, dto.getChangedBy());
        assertEquals(changedAt, dto.getChangedAt());
    }

    @Test
    void constructorShouldAllowNullReasonAndChangedAt() {
        // Arrange
        String id = "123";
        String orderId = "456";
        String initialStatus = "REJECTED";
        String finalStatus = "PENDING";
        String reason = null;
        String changedBy = "admin";
        Instant changedAt = null;

        // Act
        OrderStatusHistoryResponseDTO dto = new OrderStatusHistoryResponseDTO(
                id, orderId, initialStatus, finalStatus, reason, changedBy, changedAt
        );

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(orderId, dto.getOrderId());
        assertEquals(initialStatus, dto.getInitialStatus());
        assertEquals(finalStatus, dto.getFinalStatus());
        assertNull(dto.getReason());
        assertEquals(changedBy, dto.getChangedBy());
        assertNull(dto.getChangedAt());
    }
}