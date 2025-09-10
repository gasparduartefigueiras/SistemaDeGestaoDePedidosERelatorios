package SistemaDeGestaoDePedidosERelatorios.DTOs.order;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validDtoPassesValidation() {
        OrderRequestDTO dto = new OrderRequestDTO("Alice", "alice@example.com", 100.0);

        Set<ConstraintViolation<OrderRequestDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "DTO válido não deve ter violações");
    }

    @Test
    void blankNameFailsValidation() {
        OrderRequestDTO dto = new OrderRequestDTO("", "alice@example.com", 100.0);

        Set<ConstraintViolation<OrderRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("customerName")));
    }

    @Test
    void invalidEmailFailsValidation() {
        OrderRequestDTO dto = new OrderRequestDTO("Alice", "invalid-email", 100.0);

        Set<ConstraintViolation<OrderRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("customerEmail")));
    }

    @Test
    void negativeValueFailsValidation() {
        OrderRequestDTO dto = new OrderRequestDTO("Alice", "alice@example.com", -50.0);

        Set<ConstraintViolation<OrderRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("orderValue")));
    }

    @Test
    void gettersAndSettersWork() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setCustomerName("Bob");
        dto.setCustomerEmail("bob@example.com");
        dto.setOrderValue(200.0);

        assertEquals("Bob", dto.getCustomerName());
        assertEquals("bob@example.com", dto.getCustomerEmail());
        assertEquals(200.0, dto.getOrderValue());
    }
}