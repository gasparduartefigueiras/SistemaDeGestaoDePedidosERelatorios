package SistemaDeGestaoDePedidosERelatorios.service.order;

import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderRequestDTO;
import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IOrderService {
    OrderResponseDTO create(OrderRequestDTO request);

    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO getOrderByID(UUID id);
    List<OrderResponseDTO> findByStatus(String status);
    List<OrderResponseDTO> findByCreationDate(LocalDate from, LocalDate to);
}