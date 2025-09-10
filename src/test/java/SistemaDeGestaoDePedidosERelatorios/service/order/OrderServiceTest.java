package SistemaDeGestaoDePedidosERelatorios.service.order;

import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderRequestDTO;
import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.assembler.OrderDTOAssembler;
import SistemaDeGestaoDePedidosERelatorios.domain.order.Order;
import SistemaDeGestaoDePedidosERelatorios.domain.order.OrderFactory;
import SistemaDeGestaoDePedidosERelatorios.infrastructure.IExternalClientValidator;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.order.OrderDataModel;
import SistemaDeGestaoDePedidosERelatorios.persistence.mappers.order.IOrderMapper;
import SistemaDeGestaoDePedidosERelatorios.persistence.repositories.order.IOrderRepository;
import SistemaDeGestaoDePedidosERelatorios.service.errorLog.IErrorLogService;
import SistemaDeGestaoDePedidosERelatorios.service.orderStatus.IOrderStatusHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private IOrderRepository orderRepository;
    private IOrderMapper orderMapper;
    private OrderFactory orderFactory;           // vamos mockar o factory (não real)
    private IExternalClientValidator clientValidator;
    private IErrorLogService errorLogService;
    private IOrderStatusHistoryService historyService;

    private OrderService service;

    @BeforeEach
    void setUp() {
        orderRepository = mock(IOrderRepository.class);
        orderMapper = mock(IOrderMapper.class);
        orderFactory = mock(OrderFactory.class);
        clientValidator = mock(IExternalClientValidator.class);
        errorLogService = mock(IErrorLogService.class);
        historyService = mock(IOrderStatusHistoryService.class);

        service = new OrderService(
                orderRepository,
                orderMapper,
                orderFactory,
                clientValidator,
                errorLogService,
                historyService
        );
    }

    // ---------- helpers ----------

    private static OrderId orderId(UUID id) {
        return new OrderId(id);
    }

    private static OrderStatus os(OrderStatus.Status s) {
        return new OrderStatus(s);
    }

    private static Order mockOrderWithId(UUID id) {
        Order order = mock(Order.class);
        OrderId oid = new OrderId(id);
        when(order.getId()).thenReturn(oid);
        return order;
    }

    // ========== create() HAPPY PATHS / VARIAÇÕES ==========

    @Test
    void createApproved_RecordsHistory_Saves_AndReturnsDTO() {
        // arrange
        OrderRequestDTO req = new OrderRequestDTO("Alice Johnson", "alice@example.com", 150.0);

        UUID id = UUID.randomUUID();
        Order order = mockOrderWithId(id);

        // status inicial PENDING, depois da validação vira APPROVED
        when(order.getStatus()).thenReturn(os(OrderStatus.Status.PENDING), os(OrderStatus.Status.APPROVED));

        when(orderFactory.createOrder(
                any(CustomerName.class),
                any(CustomerEmail.class),
                any(OrderValue.class))
        ).thenReturn(order);

        ValidationOutcome outcome = new ValidationOutcome(true, "Approved (whitelist).");
        when(clientValidator.validate(any(CustomerName.class), any(CustomerEmail.class)))
                .thenReturn(outcome);

        // persistência
        OrderDataModel dmUnsaved = mock(OrderDataModel.class);
        OrderDataModel dmSaved = mock(OrderDataModel.class);
        when(orderMapper.toDataModel(order)).thenReturn(dmUnsaved);
        when(orderRepository.save(dmUnsaved)).thenReturn(dmSaved);

        // o DTO final é montado a partir do "domain after save" devolvido pelo mapper
        OrderFactory realFactoryForResponse = new OrderFactory();
        Order domainAfterSave = realFactoryForResponse.createOrder(
                new CustomerName("Alice Johnson"),
                new CustomerEmail("alice@example.com"),
                new OrderValue(150.0)
        );
        when(orderMapper.toDomain(dmSaved)).thenReturn(domainAfterSave);

        // act
        OrderResponseDTO dto = service.create(req);

        // assert
        assertNotNull(dto);
        assertEquals("alice@example.com", dto.getCustomerEmail());
        assertEquals("Alice Johnson", dto.getCustomerName());
        assertEquals(150.0, dto.getOrderValue());

        // history chamado com initial PENDING -> final APPROVED
        verify(historyService).record(
                eq(id),
                eq(os(OrderStatus.Status.PENDING)),
                eq(os(OrderStatus.Status.APPROVED)),
                eq("Approved (whitelist)."),
                eq("system")
        );

        // sem e-mails de erro/aviso
        verify(errorLogService, never()).error(anyString(), any());
        verify(errorLogService, never()).warn(anyString(), any());

        verify(orderMapper).toDataModel(order);
        verify(orderRepository).save(dmUnsaved);
        verify(orderMapper).toDomain(dmSaved);
    }

    @Test
    void createRejected_RecordsHistory_LogsError_AndSaves() {
        // arrange
        OrderRequestDTO req = new OrderRequestDTO("Kevin Ferreira", "kevinreject123@gmail.com", 45.2);

        UUID id = UUID.randomUUID();
        Order order = mockOrderWithId(id);

        when(order.getStatus()).thenReturn(os(OrderStatus.Status.PENDING), os(OrderStatus.Status.REJECTED));

        when(orderFactory.createOrder(any(CustomerName.class), any(CustomerEmail.class), any(OrderValue.class)))
                .thenReturn(order);

        ValidationOutcome outcome = new ValidationOutcome(false, "ERROR: Client rejected by WireMock.");
        when(clientValidator.validate(any(CustomerName.class), any(CustomerEmail.class)))
                .thenReturn(outcome);

        OrderDataModel dm = mock(OrderDataModel.class);
        when(orderMapper.toDataModel(order)).thenReturn(dm);
        when(orderRepository.save(dm)).thenReturn(dm);

        OrderFactory realFactoryForResponse = new OrderFactory();
        Order domainAfterSave = realFactoryForResponse.createOrder(
                new CustomerName("Kevin Ferreira"),
                new CustomerEmail("kevinreject123@gmail.com"),
                new OrderValue(45.2)
        );
        when(orderMapper.toDomain(dm)).thenReturn(domainAfterSave);

        // act
        OrderResponseDTO dto = service.create(req);

        // assert DTO
        assertNotNull(dto);
        assertEquals("Kevin Ferreira", dto.getCustomerName());

        // history + erro
        verify(historyService).record(
                eq(id),
                eq(os(OrderStatus.Status.PENDING)),
                eq(os(OrderStatus.Status.REJECTED)),
                eq("ERROR: Client rejected by WireMock."),
                eq("system")
        );
        verify(errorLogService).error(contains("Validation rejected"), eq(id));

        // sem warn
        verify(errorLogService, never()).warn(anyString(), any());
    }

    @Test
    void createPendingWhenValidationServiceUnavailable_LogsWarn_NoHistory() {
        // arrange
        OrderRequestDTO req = new OrderRequestDTO("Pedro Marques", "timeout@test.com", 410.0);

        UUID id = UUID.randomUUID();
        Order order = mockOrderWithId(id);

        // neste caminho o service NÃO chama applyValidationResult, chama setValidationResult
        when(orderFactory.createOrder(any(CustomerName.class), any(CustomerEmail.class), any(OrderValue.class)))
                .thenReturn(order);

        // outcome “especial” que ativa o caminho de indisponibilidade
        when(clientValidator.validate(any(CustomerName.class), any(CustomerEmail.class)))
                .thenReturn(new ValidationOutcome(false, "Validation service unavailable."));

        OrderDataModel dm = mock(OrderDataModel.class);
        when(orderMapper.toDataModel(order)).thenReturn(dm);
        when(orderRepository.save(dm)).thenReturn(dm);

        OrderFactory realFactoryForResponse = new OrderFactory();
        Order domainAfterSave = realFactoryForResponse.createOrder(
                new CustomerName("Pedro Marques"),
                new CustomerEmail("timeout@test.com"),
                new OrderValue(410.0)
        );
        when(orderMapper.toDomain(dm)).thenReturn(domainAfterSave);

        // act
        OrderResponseDTO dto = service.create(req);

        // assert
        assertNotNull(dto);

        // verifica que definiu resultado de validação no domínio
        verify(order).setValidationResult(argThat(vm ->
                vm != null && vm.getValidationMessage() != null &&
                        vm.getValidationMessage().contains("Validation service unavailable.")
        ));
        // sem histórico neste caminho
        verify(historyService, never()).record(any(UUID.class), any(OrderStatus.class), any(OrderStatus.class), anyString(), anyString());

        // enviou WARN
        verify(errorLogService).warn(contains("Validation pending"), eq(id));
        verify(errorLogService, never()).error(anyString(), any());
    }

    @Test
    void createWhenValidatorThrows_LogsExceptionAndRethrows() {
        // arrange
        OrderRequestDTO req = new OrderRequestDTO("Bob", "bob@test.com", 10.0);

        UUID id = UUID.randomUUID();
        Order order = mockOrderWithId(id);
        when(orderFactory.createOrder(any(CustomerName.class), any(CustomerEmail.class), any(OrderValue.class)))
                .thenReturn(order);

        RuntimeException boom = new RuntimeException("boom");
        when(clientValidator.validate(any(CustomerName.class), any(CustomerEmail.class)))
                .thenThrow(boom);

        // act + assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(req));
        assertSame(boom, ex);

        verify(errorLogService).exception(eq("Order creation failed"), eq(id), eq(boom));
        verifyNoInteractions(orderRepository);
    }

    // ========== getAll / getById / findByStatus / findByCreationDate ==========

    @Test
    void getAllOrdersMapsToDTOs() {
        OrderDataModel dm1 = mock(OrderDataModel.class);
        OrderDataModel dm2 = mock(OrderDataModel.class);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(dm1, dm2));

        OrderFactory f = new OrderFactory();
        Order o1 = f.createOrder(new CustomerName("A"), new CustomerEmail("a@x.com"), new OrderValue(1.0));
        Order o2 = f.createOrder(new CustomerName("B"), new CustomerEmail("b@x.com"), new OrderValue(2.0));

        when(orderMapper.toDomain(dm1)).thenReturn(o1);
        when(orderMapper.toDomain(dm2)).thenReturn(o2);

        assertEquals(2, service.getAllOrders().size());
    }

    @Test
    void getOrderByIdFoundReturnsDTO() {
        UUID id = UUID.randomUUID();
        OrderDataModel dm = mock(OrderDataModel.class);
        when(orderRepository.findById(id)).thenReturn(Optional.of(dm));

        OrderFactory f = new OrderFactory();
        Order o = f.createOrder(new CustomerName("A"), new CustomerEmail("a@x.com"), new OrderValue(1.0));
        when(orderMapper.toDomain(dm)).thenReturn(o);

        OrderResponseDTO dto = service.getOrderByID(id);
        assertNotNull(dto);
        assertEquals("A", dto.getCustomerName());
    }

    @Test
    void getOrderByIdNotFoundReturnsNull() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        assertNull(service.getOrderByID(id));
    }

    @Test
    void findByStatusValid() {
        OrderDataModel dm = mock(OrderDataModel.class);
        when(orderRepository.findAllByStatus("APPROVED")).thenReturn(Collections.singletonList(dm));

        OrderFactory f = new OrderFactory();
        Order o = f.createOrder(new CustomerName("A"), new CustomerEmail("a@x.com"), new OrderValue(1.0));
        when(orderMapper.toDomain(dm)).thenReturn(o);

        assertEquals(1, service.findByStatus("approved").size());
    }

    @Test
    void findByStatusInvalidThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.findByStatus("WRONG"));
        assertTrue(ex.getMessage().contains("Invalid status"));
    }

    @Test
    void findByCreationDateHappyPath() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 1, 31);

        OrderDataModel dm = mock(OrderDataModel.class);
        when(orderRepository.findAllByCreationDateBetween(from, to))
                .thenReturn(Collections.singletonList(dm));

        OrderFactory f = new OrderFactory();
        Order o = f.createOrder(new CustomerName("A"), new CustomerEmail("a@x.com"), new OrderValue(1.0));
        when(orderMapper.toDomain(dm)).thenReturn(o);

        assertEquals(1, service.findByCreationDate(from, to).size());
    }

    @Test
    void findByCreationDateToBeforeFromThrows() {
        LocalDate from = LocalDate.of(2025, 2, 1);
        LocalDate to = LocalDate.of(2025, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> service.findByCreationDate(from, to));
    }
}