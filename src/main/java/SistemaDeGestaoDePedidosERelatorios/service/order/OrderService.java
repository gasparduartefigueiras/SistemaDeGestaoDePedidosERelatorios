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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderMapper orderMapper;
    private final OrderFactory orderFactory;
    private final IExternalClientValidator clientValidator;
    private final IErrorLogService errorLogService;
    private final IOrderStatusHistoryService orderStatusHistoryService;

    public OrderService(IOrderRepository orderRepository,
                        IOrderMapper orderMapper,
                        OrderFactory orderFactory,
                        IExternalClientValidator clientValidator,
                        IErrorLogService errorLogService,
                        IOrderStatusHistoryService orderStatusHistoryService) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "orderRepository");
        this.orderMapper = Objects.requireNonNull(orderMapper, "orderMapper");
        this.orderFactory = Objects.requireNonNull(orderFactory, "orderFactory");
        this.clientValidator = Objects.requireNonNull(clientValidator, "clientValidator");
        this.errorLogService = Objects.requireNonNull(errorLogService, "errorLogService");
        this.orderStatusHistoryService = Objects.requireNonNull(orderStatusHistoryService, "orderStatusHistoryService");
    }

    @Override
    public OrderResponseDTO create(OrderRequestDTO request) {
        CustomerName name = new CustomerName(request.getCustomerName());
        CustomerEmail email = new CustomerEmail(request.getCustomerEmail());
        OrderValue value = new OrderValue(request.getOrderValue());

        Order order = orderFactory.createOrder(name, email, value);
        OrderStatus initialStatus = order.getStatus();

        try {
            ValidationOutcome outcome = clientValidator.validate(name, email);

            boolean isUnavailable = outcome != null
                    && !outcome.isApproved()
                    && outcome.getMessage() != null
                    && outcome.getMessage().trim().equalsIgnoreCase("Validation service unavailable.");


            if (isUnavailable) {
                order.setValidationResult(ValidationMessage.error("Validation service unavailable."));
                UUID orderId = order.getId().getOrderId();
                errorLogService.warn("Validation pending: Validation service unavailable.", orderId);
            } else {
                order.applyValidationResult(outcome);

                OrderStatus finalStatus = order.getStatus();
                if (!finalStatus.equals(initialStatus)) {
                    orderStatusHistoryService.record(
                            order.getId().getOrderId(),
                            initialStatus,
                            finalStatus,
                            outcome.getMessage(),
                            "system"
                    );
                }

                UUID orderId = order.getId().getOrderId();
                if (finalStatus.getOrderStatus() == OrderStatus.Status.REJECTED) {
                    errorLogService.error("Validation rejected: " + outcome.getMessage(), orderId);
                } else if (finalStatus.getOrderStatus() == OrderStatus.Status.PENDING) {
                    errorLogService.warn("Validation pending: " + outcome.getMessage(), orderId);
                }
            }

            OrderDataModel saved = orderRepository.save(orderMapper.toDataModel(order));
            return OrderDTOAssembler.toResponseDTO(orderMapper.toDomain(saved));

        } catch (Exception ex) {
            UUID orderId = (order.getId() != null) ? order.getId().getOrderId() : null;
            errorLogService.exception("Order creation failed", orderId, ex);
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDomain)
                .map(OrderDTOAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderByID(UUID id) {
        Objects.requireNonNull(id, "id");
        return orderRepository.findById(id)
                .map(orderMapper::toDomain)
                .map(OrderDTOAssembler::toResponseDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findByStatus(String status) {
        Objects.requireNonNull(status, "status");
        final String normalized;
        try {
            normalized = OrderStatus.Status.valueOf(status.trim().toUpperCase()).name();
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status. Allowed: PENDING, APPROVED, REJECTED");
        }

        return orderRepository.findAllByStatus(normalized)
                .stream()
                .map(orderMapper::toDomain)
                .map(OrderDTOAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findByCreationDate(LocalDate from, LocalDate to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("'to' must be on/after 'from'");
        }

        return orderRepository.findAllByCreationDateBetween(from, to)
                .stream()
                .map(orderMapper::toDomain)
                .map(OrderDTOAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
}