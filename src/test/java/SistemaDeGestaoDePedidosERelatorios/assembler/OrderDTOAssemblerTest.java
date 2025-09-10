package SistemaDeGestaoDePedidosERelatorios.assembler;

import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerEmail;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerName;
import SistemaDeGestaoDePedidosERelatorios.VOs.OrderValue;
import SistemaDeGestaoDePedidosERelatorios.VOs.ValidationOutcome;
import SistemaDeGestaoDePedidosERelatorios.domain.order.Order;
import SistemaDeGestaoDePedidosERelatorios.domain.order.OrderFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderDTOAssemblerTest {

    @Test
    void toResponseDTOMapsAllFieldsWhenValidationMessageIsNull() {
        // arrange (factory cria PENDING sem validationMessage)
        OrderFactory factory = new OrderFactory();
        Order order = factory.createOrder(
                new CustomerName("Alice"),
                new CustomerEmail("alice@example.com"),
                new OrderValue(100.0)
        );

        // act
        OrderResponseDTO dto = OrderDTOAssembler.toResponseDTO(order);

        // assert
        assertEquals(order.getId().getOrderId().toString(), dto.getId());
        assertEquals("Alice", dto.getCustomerName());
        assertEquals("alice@example.com", dto.getCustomerEmail());
        assertEquals(100.0, dto.getOrderValue(), 0.0001);
        assertEquals(order.getStatus().getOrderStatus().name(), dto.getStatus());
        assertEquals(order.getCreationDate().getCreationDate().toString(), dto.getCreatedAt());
        // ramo: validationMessage == null
        assertNull(dto.getValidationMessage());
    }

    @Test
    void toResponseDTOMapsValidationMessageWhenPresent() {
        // arrange
        OrderFactory factory = new OrderFactory();
        Order order = factory.createOrder(
                new CustomerName("Bob"),
                new CustomerEmail("bob@example.com"),
                new OrderValue(250.0)
        );
        // provoca definição de validationMessage no domínio
        order.applyValidationResult(new ValidationOutcome(true, "Approved by external validator"));

        // act
        OrderResponseDTO dto = OrderDTOAssembler.toResponseDTO(order);

        // assert
        assertEquals("Bob", dto.getCustomerName());
        assertEquals("bob@example.com", dto.getCustomerEmail());
        // ramo: validationMessage != null
        assertEquals("Approved by external validator", dto.getValidationMessage());
    }

    @Test
    void toResponseDTONullOrderThrowsException() {
        assertThrows(NullPointerException.class, () -> OrderDTOAssembler.toResponseDTO(null));
    }
}