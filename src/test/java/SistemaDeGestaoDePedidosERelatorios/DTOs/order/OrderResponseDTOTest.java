package SistemaDeGestaoDePedidosERelatorios.DTOs.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderResponseDTOTest {

    @Test
    void allArgsConstructorAssignsFieldsCorrectly() {
        OrderResponseDTO dto = new OrderResponseDTO(
                "1",
                "Alice",
                "alice@example.com",
                100.0,
                "APPROVED",
                "2025-09-06T12:00:00",
                "Valid"
        );

        assertEquals("1", dto.getId());
        assertEquals("Alice", dto.getCustomerName());
        assertEquals("alice@example.com", dto.getCustomerEmail());
        assertEquals(100.0, dto.getOrderValue());
        assertEquals("APPROVED", dto.getStatus());
        assertEquals("2025-09-06T12:00:00", dto.getCreatedAt());
        assertEquals("Valid", dto.getValidationMessage());
    }

    @Test
    void noArgsConstructorAndSettersWorkCorrectly() {
        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setId("2");
        dto.setCustomerName("Bob");
        dto.setCustomerEmail("bob@example.com");
        dto.setOrderValue(200.0);
        dto.setStatus("PENDING");
        dto.setCreatedAt("2025-09-06T13:30:00");
        dto.setValidationMessage("Waiting");

        assertEquals("2", dto.getId());
        assertEquals("Bob", dto.getCustomerName());
        assertEquals("bob@example.com", dto.getCustomerEmail());
        assertEquals(200.0, dto.getOrderValue());
        assertEquals("PENDING", dto.getStatus());
        assertEquals("2025-09-06T13:30:00", dto.getCreatedAt());
        assertEquals("Waiting", dto.getValidationMessage());
    }
}