package SistemaDeGestaoDePedidosERelatorios.service.orderStatus;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistory;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistoryFactory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.orderStatusHistory.OrderStatusHistoryDataModel;
import SistemaDeGestaoDePedidosERelatorios.persistence.mappers.orderStatusHistory.IOrderStatusHistoryMapper;
import SistemaDeGestaoDePedidosERelatorios.persistence.repositories.orderStatusHistory.IOrderStatusHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderStatusHistoryService implements IOrderStatusHistoryService {

    private final IOrderStatusHistoryRepository repo;
    private final IOrderStatusHistoryMapper mapper;
    private final OrderStatusHistoryFactory factory;

    public OrderStatusHistoryService(IOrderStatusHistoryRepository repo,
                                     IOrderStatusHistoryMapper mapper,
                                     OrderStatusHistoryFactory factory) {
        this.repo = Objects.requireNonNull(repo);
        this.mapper = Objects.requireNonNull(mapper);
        this.factory = Objects.requireNonNull(factory);
    }

    @Override
    public List<OrderStatusHistory> findAll() {
        return repo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderStatusHistory> findById(UUID id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<OrderStatusHistory> findByOrderId(UUID orderId) {
        return repo.findByOrderIdOrderByChangedAtAsc(orderId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = false)
    public void record(UUID orderId,
                       OrderStatus initialStatus,
                       OrderStatus finalStatus,
                       String reason,
                       String changedBy) {

        StatusChangeReason reasonVO;
        if (reason == null || reason.trim().isEmpty()) {
            reasonVO = null;
        } else {
            reasonVO = new StatusChangeReason(reason.trim());
        }

        String by = (changedBy == null || changedBy.trim().isEmpty()) ? "system" : changedBy.trim();

        OrderStatusHistory domain = factory.createHistory(
                new OrderId(orderId),
                initialStatus,
                finalStatus,
                reasonVO,
                new ChangedBy(by)
        );

        OrderStatusHistoryDataModel dm = mapper.toDataModel(domain);
        repo.save(dm);
    }
}