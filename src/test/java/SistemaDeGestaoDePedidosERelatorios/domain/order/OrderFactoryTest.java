package SistemaDeGestaoDePedidosERelatorios.domain.order;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.UUID;

class OrderFactoryTest {

    private final OrderFactory factory = new OrderFactory();

    @Test
    void shouldCreateOrderWithValidParameters() {
        CustomerName name = new CustomerName("João Silva");
        CustomerEmail email = new CustomerEmail("joao@email.com");
        OrderValue value = new OrderValue(150.75);

        Order order = factory.createOrder(name, email, value);

        assertNotNull(order);
        assertEquals(name, order.getCustomerName());
        assertEquals(email, order.getCustomerEmail());
        assertEquals(value, order.getOrderValue());
        assertNotNull(order.getId());
        assertNotNull(order.getCreationDate());
        assertEquals(OrderStatus.Status.PENDING, order.getStatus().getOrderStatus());
        assertNull(order.getValidationMessage());
        assertFalse(order.isValidated());
    }

    @Test
    void shouldCreateOrderWithCurrentDate() {
        CustomerName name = new CustomerName("Maria Santos");
        CustomerEmail email = new CustomerEmail("maria@email.com");
        OrderValue value = new OrderValue(200.00);

        LocalDate before = LocalDate.now();
        Order order = factory.createOrder(name, email, value);
        LocalDate after = LocalDate.now();

        LocalDate orderDate = order.getCreationDate().getCreationDate();
        assertTrue(orderDate.equals(before) || orderDate.equals(after));
    }

    @Test
    void shouldCreateOrderWithUniqueId() {
        CustomerName name = new CustomerName("Pedro Costa");
        CustomerEmail email = new CustomerEmail("pedro@email.com");
        OrderValue value = new OrderValue(100.00);

        Order order1 = factory.createOrder(name, email, value);
        Order order2 = factory.createOrder(name, email, value);

        assertNotEquals(order1.getId(), order2.getId());
    }

    @Test
    void shouldCreateOrderAlwaysWithPendingStatus() {
        CustomerName name = new CustomerName("Ana Silva");
        CustomerEmail email = new CustomerEmail("ana@email.com");
        OrderValue value = new OrderValue(75.50);

        Order order = factory.createOrder(name, email, value);

        assertEquals(OrderStatus.Status.PENDING, order.getStatus().getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNameIsNull() {
        CustomerEmail email = new CustomerEmail("test@email.com");
        OrderValue value = new OrderValue(100.00);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createOrder(null, email, value)
        );

        assertEquals("Name, Email and OrderValue cannot be null.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerEmailIsNull() {
        CustomerName name = new CustomerName("João Silva");
        OrderValue value = new OrderValue(100.00);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createOrder(name, null, value)
        );

        assertEquals("Name, Email and OrderValue cannot be null.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOrderValueIsNull() {
        CustomerName name = new CustomerName("João Silva");
        CustomerEmail email = new CustomerEmail("joao@email.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createOrder(name, email, null)
        );

        assertEquals("Name, Email and OrderValue cannot be null.", exception.getMessage());
    }

    @Test
    void shouldRecreateOrderWithAllParameters() {
        OrderId id = new OrderId();
        CustomerName name = new CustomerName("Carlos Pereira");
        CustomerEmail email = new CustomerEmail("carlos@email.com");
        OrderValue value = new OrderValue(300.25);
        CreationDate date = new CreationDate(LocalDate.of(2025, 1, 15));
        OrderStatus status = OrderStatus.approved();
        ValidationMessage message = ValidationMessage.success(null);

        Order order = factory.recreateOrder(name, email, value, date, id, status, message);

        assertEquals(id, order.getId());
        assertEquals(name, order.getCustomerName());
        assertEquals(email, order.getCustomerEmail());
        assertEquals(value, order.getOrderValue());
        assertEquals(date, order.getCreationDate());
        assertEquals(status, order.getStatus());
        assertEquals(message, order.getValidationMessage());
        assertTrue(order.isValidated());
    }

    @Test
    void shouldRecreateOrderWithNullValidationMessage() {
        OrderId id = new OrderId();
        CustomerName name = new CustomerName("Sofia Lima");
        CustomerEmail email = new CustomerEmail("sofia@email.com");
        OrderValue value = new OrderValue(125.00);
        CreationDate date = new CreationDate();
        OrderStatus status = OrderStatus.pending();

        Order order = factory.recreateOrder(name, email, value, date, id, status, null);

        assertNull(order.getValidationMessage());
        assertFalse(order.isValidated());
    }

    @Test
    void shouldRecreateOrderWithRejectedStatus() {
        OrderId id = new OrderId();
        CustomerName name = new CustomerName("Ricardo Santos");
        CustomerEmail email = new CustomerEmail("ricardo@email.com");
        OrderValue value = new OrderValue(50.00);
        CreationDate date = new CreationDate();
        OrderStatus status = OrderStatus.rejected();
        ValidationMessage message = ValidationMessage.error("Client not found");

        Order order = factory.recreateOrder(name, email, value, date, id, status, message);

        assertEquals(OrderStatus.Status.REJECTED, order.getStatus().getOrderStatus());
        assertEquals(message, order.getValidationMessage());
    }

    @Test
    void shouldRecreateOrderExactlyAsProvided() {
        UUID specificUuid = UUID.randomUUID();
        OrderId id = new OrderId(specificUuid);
        CustomerName name = new CustomerName("Test User");
        CustomerEmail email = new CustomerEmail("test@test.com");
        OrderValue value = new OrderValue(999.99);
        CreationDate date = new CreationDate(LocalDate.of(2020, 12, 25));
        OrderStatus status = OrderStatus.approved();
        ValidationMessage message = ValidationMessage.error("Test error");

        Order order = factory.recreateOrder(name, email, value, date, id, status, message);

        assertEquals(specificUuid, order.getId().getOrderId());
        assertEquals("Test User", order.getCustomerName().getCustomerName());
        assertEquals("test@test.com", order.getCustomerEmail().getEmail());
        assertEquals(999.99, order.getOrderValue().getOrderValue());
        assertEquals(LocalDate.of(2020, 12, 25), order.getCreationDate().getCreationDate());
        assertEquals(OrderStatus.Status.APPROVED, order.getStatus().getOrderStatus());
        assertEquals("ERROR: Test error", order.getValidationMessage().getValidationMessage());
    }
}