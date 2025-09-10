package SistemaDeGestaoDePedidosERelatorios.DTOs.errorLog;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ErrorLogResponseDTOTest {

    @Test
    void constructorAndGettersShouldWorkCorrectly() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String level = "ERROR";
        String message = "Something went wrong";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        ErrorLogResponseDTO dto = new ErrorLogResponseDTO(id, orderId, level, message, timestamp);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(orderId, dto.getOrderId());
        assertEquals(level, dto.getLevel());
        assertEquals(message, dto.getMessage());
        assertEquals(timestamp, dto.getTimestamp());
    }

    @Test
    void constructorShouldAcceptNullOrderId() {
        // Arrange
        UUID id = UUID.randomUUID();
        String level = "WARN";
        String message = "OrderId is null";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        ErrorLogResponseDTO dto = new ErrorLogResponseDTO(id, null, level, message, timestamp);

        // Assert
        assertEquals(id, dto.getId());
        assertNull(dto.getOrderId());
        assertEquals(level, dto.getLevel());
        assertEquals(message, dto.getMessage());
        assertEquals(timestamp, dto.getTimestamp());
    }
}