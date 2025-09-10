package SistemaDeGestaoDePedidosERelatorios.domain.order;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class OrderTest {

    private Order createValidOrder() {
        OrderId id = new OrderId();
        CustomerName name = new CustomerName("João Silva");
        CustomerEmail email = new CustomerEmail("joao@email.com");
        OrderValue value = new OrderValue(100.50);
        CreationDate date = new CreationDate();
        OrderStatus status = OrderStatus.pending();
        ValidationMessage validationMessage = null;

        return new Order(id, name, email, value, date, status, validationMessage);
    }

    @Test
    void shouldCreateOrderWithValidParameters() {
        OrderId id = new OrderId();
        CustomerName name = new CustomerName("Maria Santos");
        CustomerEmail email = new CustomerEmail("maria@email.com");
        OrderValue value = new OrderValue(250.75);
        CreationDate date = new CreationDate();
        OrderStatus status = OrderStatus.pending();
        ValidationMessage validationMessage = ValidationMessage.success(null);

        Order order = new Order(id, name, email, value, date, status, validationMessage);

        assertEquals(id, order.getId());
        assertEquals(name, order.getCustomerName());
        assertEquals(email, order.getCustomerEmail());
        assertEquals(value, order.getOrderValue());
        assertEquals(date, order.getCreationDate());
        assertEquals(status, order.getStatus());
        assertEquals(validationMessage, order.getValidationMessage());
    }

    @Test
    void shouldCreateOrderWithNullValidationMessage() {
        Order order = createValidOrder();

        assertNull(order.getValidationMessage());
        assertFalse(order.isValidated());
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Order(null, new CustomerName("João"), new CustomerEmail("joao@email.com"),
                        new OrderValue(100.0), new CreationDate(), OrderStatus.pending(), null)
        );

        assertEquals("OrderId cannot be null.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNameIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Order(new OrderId(), null, new CustomerEmail("joao@email.com"),
                        new OrderValue(100.0), new CreationDate(), OrderStatus.pending(), null)
        );

        assertEquals("CustomerName cannot be null.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerEmailIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Order(new OrderId(), new CustomerName("João"), null,
                        new OrderValue(100.0), new CreationDate(), OrderStatus.pending(), null)
        );

        assertEquals("CustomerEmail cannot be null.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOrderValueIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Order(new OrderId(), new CustomerName("João"), new CustomerEmail("joao@email.com"),
                        null, new CreationDate(), OrderStatus.pending(), null)
        );

        assertEquals("OrderValue cannot be null.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCreationDateIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Order(new OrderId(), new CustomerName("João"), new CustomerEmail("joao@email.com"),
                        new OrderValue(100.0), null, OrderStatus.pending(), null)
        );

        assertEquals("CreationDate cannot be null.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOrderStatusIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Order(new OrderId(), new CustomerName("João"), new CustomerEmail("joao@email.com"),
                        new OrderValue(100.0), new CreationDate(), null, null)
        );

        assertEquals("OrderStatus cannot be null.", exception.getMessage());
    }

    @Test
    void shouldApproveOrderWhenStatusIsPending() {
        Order order = createValidOrder();

        order.approve();

        assertEquals(OrderStatus.Status.APPROVED, order.getStatus().getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenTryingToApproveNonPendingOrder() {
        Order order = createValidOrder();
        order.approve(); // First approval

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> order.approve() // Second approval
        );

        assertEquals("Only pending orders can be approved.", exception.getMessage());
    }

    @Test
    void shouldRejectOrderWhenStatusIsPending() {
        Order order = createValidOrder();

        order.reject();

        assertEquals(OrderStatus.Status.REJECTED, order.getStatus().getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenTryingToRejectNonPendingOrder() {
        Order order = createValidOrder();
        order.reject(); // First rejection

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> order.reject() // Second rejection
        );

        assertEquals("Only pending orders can be rejected.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTryingToRejectApprovedOrder() {
        Order order = createValidOrder();
        order.approve();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> order.reject()
        );

        assertEquals("Only pending orders can be rejected.", exception.getMessage());
    }

    @Test
    void shouldSetValidationResult() {
        Order order = createValidOrder();
        ValidationMessage validationMessage = ValidationMessage.success("Valid Client");

        order.setValidationResult(validationMessage);

        assertEquals(validationMessage, order.getValidationMessage());
        assertTrue(order.isValidated());
    }

    @Test
    void shouldSetValidationErrorResult() {
        Order order = createValidOrder();
        ValidationMessage validationMessage = ValidationMessage.error("Client not found");

        order.setValidationResult(validationMessage);

        assertEquals(validationMessage, order.getValidationMessage());
        assertTrue(order.isValidated());
    }

    @Test
    void shouldReturnFalseForIsValidatedWhenValidationMessageIsNull() {
        Order order = createValidOrder();

        assertFalse(order.isValidated());
    }

    @Test
    void shouldBeEqualWhenSameId() {
        UUID sameUuid = UUID.randomUUID();
        OrderId id = new OrderId(sameUuid);

        Order order1 = new Order(id, new CustomerName("João"), new CustomerEmail("joao@email.com"),
                new OrderValue(100.0), new CreationDate(), OrderStatus.pending(), null);
        Order order2 = new Order(id, new CustomerName("Maria"), new CustomerEmail("maria@email.com"),
                new OrderValue(200.0), new CreationDate(), OrderStatus.approved(), null);

        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        Order order1 = createValidOrder();
        Order order2 = createValidOrder();

        assertNotEquals(order1, order2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        Order order = createValidOrder();

        assertNotEquals(order, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        Order order = createValidOrder();
        String notOrder = "Not an order";

        assertNotEquals(order, notOrder);
    }

    @Test
    void shouldHaveCorrectToStringFormat() {
        Order order = createValidOrder();

        String toString = order.toString();
        assertTrue(toString.startsWith("Order{id="));
        assertTrue(toString.contains("status="));
    }

    @Test
    void applyValidationResultShouldApproveAndSetSuccessMessage() {
        Order order = createValidOrder();
        ValidationOutcome outcome = new ValidationOutcome(true, "SUCCESS: Client OK");

        order.applyValidationResult(outcome);

        assertTrue(order.isValidated());
        assertNotNull(order.getValidationMessage());
        assertEquals("SUCCESS: Client OK", order.getValidationMessage().getValidationMessage());
        assertEquals(OrderStatus.Status.APPROVED, order.getStatus().getOrderStatus());
    }

    @Test
    void applyValidationResultShouldRejectAndSetErrorMessage() {
        Order order = createValidOrder();
        ValidationOutcome outcome = new ValidationOutcome(false, "Client not found");

        order.applyValidationResult(outcome);

        assertTrue(order.isValidated());
        assertNotNull(order.getValidationMessage());
        assertEquals("ERROR: Client not found", order.getValidationMessage().getValidationMessage());
        assertEquals(OrderStatus.Status.REJECTED, order.getStatus().getOrderStatus());
    }

    @Test
    void applyValidationResultShouldThrowWhenOutcomeIsNull() {
        Order order = createValidOrder();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> order.applyValidationResult(null)
        );
        assertEquals("Validation outcome cannot be null.", ex.getMessage());
    }

    @Test
    void applyValidationResultShouldThrowWhenStatusIsNotPending() {
        Order order = createValidOrder();
        order.applyValidationResult(new ValidationOutcome(true, "OK"));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> order.applyValidationResult(new ValidationOutcome(true, "Second validation"))
        );
        assertEquals("Validation can only be applied to PENDING orders.", ex.getMessage());
    }

    @Test
    void applyValidationResultShouldThrowWhenAlreadyHasValidationMessage() {
        Order order = createValidOrder();
        order.setValidationResult(ValidationMessage.success("Already validated"));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> order.applyValidationResult(new ValidationOutcome(true, "Another one"))
        );
        assertEquals("Order already has a validation result.", ex.getMessage());
    }
}