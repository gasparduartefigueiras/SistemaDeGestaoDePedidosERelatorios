package SistemaDeGestaoDePedidosERelatorios.persistence.repositories.order;

import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderRequestDTO;
import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.order.OrderDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IOrderRepository extends JpaRepository<OrderDataModel, UUID> {

    List<OrderDataModel> findAllByStatus(String status);

    List<OrderDataModel> findAllByCreationDate(LocalDate date);

    List<OrderDataModel> findAllByCreationDateBetween(LocalDate from, LocalDate to);
}