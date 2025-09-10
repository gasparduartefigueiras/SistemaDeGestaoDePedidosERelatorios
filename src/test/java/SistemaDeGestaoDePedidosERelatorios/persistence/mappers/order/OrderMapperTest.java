package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.order;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.domain.order.IOrderFactory;
import SistemaDeGestaoDePedidosERelatorios.domain.order.Order;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.order.OrderDataModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderMapperTest {

    private OrderMapper mapperWith(IOrderFactory factory) {
        return new OrderMapper(factory);
    }

    @Test
    void toDataModelMapsAllFieldsCorrectly() {
        IOrderFactory factory = mock(IOrderFactory.class);
        OrderMapper mapper = mapperWith(factory);

        UUID id = UUID.randomUUID();
        LocalDate date = LocalDate.of(2025, 9, 1);

        Order domain = new Order(
                new OrderId(id),
                new CustomerName("Ana"),
                new CustomerEmail("ana@example.com"),
                new OrderValue(100.0),
                new CreationDate(date),
                new OrderStatus(OrderStatus.Status.PENDING),
                new ValidationMessage("OK")
        );

        OrderDataModel dm = mapper.toDataModel(domain);

        assertEquals(id, dm.getId());
        assertEquals("Ana", dm.getCustomerName());
        assertEquals("ana@example.com", dm.getCustomerEmail());
        assertEquals(100.0, dm.getOrderValue(), 0.0001);
        assertEquals(date, dm.getCreationDate());
        assertEquals("PENDING", dm.getStatus());
        assertEquals("OK", dm.getValidationMessage());
    }

    @Test
    void toDataModelMapsNullValidationMessageToNull() {
        IOrderFactory factory = mock(IOrderFactory.class);
        OrderMapper mapper = mapperWith(factory);

        UUID id = UUID.randomUUID();
        Order domain = new Order(
                new OrderId(id),
                new CustomerName("Ana"),
                new CustomerEmail("ana@example.com"),
                new OrderValue(10.0),
                new CreationDate(LocalDate.of(2025, 9, 1)),
                new OrderStatus(OrderStatus.Status.APPROVED),
                null
        );

        OrderDataModel dm = mapper.toDataModel(domain);
        assertNull(dm.getValidationMessage());
        assertEquals("APPROVED", dm.getStatus());
    }

    @Test
    void toDomainUsesFactoryAndReturnsOrder() {
        IOrderFactory factory = mock(IOrderFactory.class);
        OrderMapper mapper = mapperWith(factory);

        UUID id = UUID.randomUUID();
        LocalDate date = LocalDate.of(2025, 9, 2);
        OrderDataModel dm = new OrderDataModel(
                id, "Ana", "ana@example.com", 99.99, date, "REJECTED", "Bad data"
        );

        when(factory.recreateOrder(any(), any(), any(), any(), any(), any(), any()))
                .thenAnswer(inv -> new Order(
                        (OrderId) inv.getArgument(4),
                        (CustomerName) inv.getArgument(0),
                        (CustomerEmail) inv.getArgument(1),
                        (OrderValue) inv.getArgument(2),
                        (CreationDate) inv.getArgument(3),
                        (OrderStatus) inv.getArgument(5),
                        (ValidationMessage) inv.getArgument(6)
                ));

        Order result = mapper.toDomain(dm);

        assertEquals(id, result.getId().getOrderId());
        assertEquals("Ana", result.getCustomerName().getCustomerName());
        assertEquals("ana@example.com", result.getCustomerEmail().getEmail());
        assertEquals(99.99, result.getOrderValue().getOrderValue(), 0.0001);
        assertEquals(date, result.getCreationDate().getCreationDate());
        assertEquals(OrderStatus.Status.REJECTED, result.getStatus().getOrderStatus());
        assertEquals("Bad data", result.getValidationMessage().getValidationMessage());
    }

    @Test
    void toDomainAllowsNullValidationMessage() {
        IOrderFactory factory = mock(IOrderFactory.class);
        OrderMapper mapper = mapperWith(factory);

        UUID id = UUID.randomUUID();
        LocalDate date = LocalDate.of(2025, 9, 1);
        OrderDataModel dm = new OrderDataModel(
                id, "Ana", "ana@example.com", 1.23, date, "APPROVED", null
        );

        when(factory.recreateOrder(any(), any(), any(), any(), any(), any(), any()))
                .thenAnswer(inv -> new Order(
                        (OrderId) inv.getArgument(4),
                        (CustomerName) inv.getArgument(0),
                        (CustomerEmail) inv.getArgument(1),
                        (OrderValue) inv.getArgument(2),
                        (CreationDate) inv.getArgument(3),
                        (OrderStatus) inv.getArgument(5),
                        (ValidationMessage) inv.getArgument(6)
                ));

        Order result = mapper.toDomain(dm);

        assertNull(result.getValidationMessage());
        assertEquals(OrderStatus.Status.APPROVED, result.getStatus().getOrderStatus());
    }

    @Test
    void toDomainThrowsIllegalArgumentWhenStatusIsInvalid() {
        IOrderFactory factory = mock(IOrderFactory.class);
        OrderMapper mapper = mapperWith(factory);

        OrderDataModel dm = new OrderDataModel(
                UUID.randomUUID(), "Ana", "ana@example.com", 1.0,
                LocalDate.of(2025, 9, 1), "NOT_A_STATUS", "OK"
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toDomain(dm)
        );
        assertTrue(ex.getMessage().contains("Error trying to map OrderDataModel to Order"));
        verify(factory, never()).recreateOrder(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void toDomainThrowsIllegalArgumentWhenDataModelIsNull() {
        IOrderFactory factory = mock(IOrderFactory.class);
        OrderMapper mapper = mapperWith(factory);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toDomain(null)
        );
        assertTrue(ex.getMessage().contains("Error trying to map OrderDataModel to Order"));
        verify(factory, never()).recreateOrder(any(), any(), any(), any(), any(), any(), any());
    }
}