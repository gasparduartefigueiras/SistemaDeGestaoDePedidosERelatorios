package SistemaDeGestaoDePedidosERelatorios.service.orderStatus;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistory;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistoryFactory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.orderStatusHistory.OrderStatusHistoryDataModel;
import SistemaDeGestaoDePedidosERelatorios.persistence.mappers.orderStatusHistory.IOrderStatusHistoryMapper;
import SistemaDeGestaoDePedidosERelatorios.persistence.repositories.orderStatusHistory.IOrderStatusHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusHistoryServiceTest {

    @Mock IOrderStatusHistoryRepository repo;
    @Mock IOrderStatusHistoryMapper mapper;
    @Mock OrderStatusHistoryFactory factory;

    @InjectMocks OrderStatusHistoryService service;

    private UUID histId;
    private UUID orderId;

    @BeforeEach
    void init() {
        histId = UUID.randomUUID();
        orderId = UUID.randomUUID();
    }

    @Test
    void findAllMapsAllRows() {
        // arrange
        OrderStatusHistoryDataModel dm1 = dm(histId, orderId, "PENDING", "APPROVED", "ok", "admin");
        OrderStatusHistoryDataModel dm2 = dm(UUID.randomUUID(), orderId, "APPROVED", "REJECTED", null, "system");
        when(repo.findAll()).thenReturn(Arrays.asList(dm1, dm2));

        OrderStatusHistory d1 = mock(OrderStatusHistory.class);
        OrderStatusHistory d2 = mock(OrderStatusHistory.class);
        when(mapper.toDomain(dm1)).thenReturn(d1);
        when(mapper.toDomain(dm2)).thenReturn(d2);

        // act
        List<OrderStatusHistory> result = service.findAll();

        // assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(d1, d2)));
        verify(repo).findAll();
        verify(mapper).toDomain(dm1);
        verify(mapper).toDomain(dm2);
    }

    @Test
    void findByIdReturnsMappedOptional() {
        // arrange
        OrderStatusHistoryDataModel dm = dm(histId, orderId, "PENDING", "APPROVED", "ok", "admin");
        when(repo.findById(histId)).thenReturn(Optional.of(dm));

        OrderStatusHistory domain = mock(OrderStatusHistory.class);
        when(mapper.toDomain(dm)).thenReturn(domain);

        // act
        Optional<OrderStatusHistory> result = service.findById(histId);

        // assert
        assertTrue(result.isPresent());
        assertSame(domain, result.get());
        verify(repo).findById(histId);
        verify(mapper).toDomain(dm);
    }

    @Test
    void findByOrderIdReturnsMappedList() {
        // arrange
        OrderStatusHistoryDataModel dm = dm(UUID.randomUUID(), orderId, "PENDING", "REJECTED", "nope", "qa");
        when(repo.findByOrderIdOrderByChangedAtAsc(orderId)).thenReturn(Collections.singletonList(dm));

        OrderStatusHistory domain = mock(OrderStatusHistory.class);
        when(mapper.toDomain(dm)).thenReturn(domain);

        // act
        List<OrderStatusHistory> result = service.findByOrderId(orderId);

        // assert
        assertEquals(1, result.size());
        assertSame(domain, result.get(0));
        verify(repo).findByOrderIdOrderByChangedAtAsc(orderId);
        verify(mapper).toDomain(dm);
    }

    @Test
    void recordWithReasonAndChangedBySaves() {
        // arrange
        OrderStatus initial = new OrderStatus(OrderStatus.Status.PENDING);
        OrderStatus fin = new OrderStatus(OrderStatus.Status.APPROVED);
        String reason = "manual approval";
        String by = "admin";

        OrderStatusHistory domain = mock(OrderStatusHistory.class);
        when(factory.createHistory(
                any(OrderId.class),
                eq(initial),
                eq(fin),
                any(StatusChangeReason.class),
                any(ChangedBy.class))
        ).thenReturn(domain);

        OrderStatusHistoryDataModel dm = dm(UUID.randomUUID(), orderId,
                initial.getOrderStatus().name(), fin.getOrderStatus().name(), reason, by);
        when(mapper.toDataModel(domain)).thenReturn(dm);

        // act
        service.record(orderId, initial, fin, reason, by);

        // assert
        ArgumentCaptor<OrderId> orderIdCap = ArgumentCaptor.forClass(OrderId.class);
        ArgumentCaptor<StatusChangeReason> reasonCap = ArgumentCaptor.forClass(StatusChangeReason.class);
        ArgumentCaptor<ChangedBy> byCap = ArgumentCaptor.forClass(ChangedBy.class);

        verify(factory).createHistory(
                orderIdCap.capture(),
                eq(initial),
                eq(fin),
                reasonCap.capture(),
                byCap.capture()
        );
        assertEquals(orderId, orderIdCap.getValue().getOrderId());
        assertEquals("manual approval", reasonCap.getValue().getReason());
        assertEquals("admin", byCap.getValue().getValue());

        verify(mapper).toDataModel(domain);
        verify(repo).save(dm);
    }

    @Test
    void recordWithEmptyReasonAndNullChangedByDefaultsProperly() {
        // arrange
        OrderStatus initial = new OrderStatus(OrderStatus.Status.PENDING);
        OrderStatus fin = new OrderStatus(OrderStatus.Status.REJECTED);

        OrderStatusHistory domain = mock(OrderStatusHistory.class);

        when(factory.createHistory(
                any(OrderId.class),
                eq(initial),
                eq(fin),
                (StatusChangeReason) isNull(),
                argThat(cb -> cb != null && "system".equals(cb.getValue()))
        )).thenReturn(domain);

        OrderStatusHistoryDataModel dm = dm(
                UUID.randomUUID(),
                orderId,
                "PENDING",
                "REJECTED",
                null,
                "system"
        );
        when(mapper.toDataModel(domain)).thenReturn(dm);

        // act
        service.record(orderId, initial, fin, "   ", null);

        // assert
        verify(factory).createHistory(
                any(OrderId.class),
                eq(initial),
                eq(fin),
                (StatusChangeReason) isNull(),
                argThat(cb -> "system".equals(cb.getValue()))
        );
        verify(mapper).toDataModel(domain);
        verify(repo).save(dm);
    }

    private static OrderStatusHistoryDataModel dm(UUID id, UUID orderId,
                                                  String initial, String fin, String reason, String by) {
        return new OrderStatusHistoryDataModel(
                id,
                orderId,
                initial,
                fin,
                reason,
                by,
                LocalDateTime.now()
        );
    }
}