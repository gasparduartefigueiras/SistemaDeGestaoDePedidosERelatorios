package SistemaDeGestaoDePedidosERelatorios.persistence.repositories.errorLog;

import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.errorLog.ErrorLogDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface IErrorLogRepository extends JpaRepository<ErrorLogDataModel, UUID> {
    Page<ErrorLogDataModel> findByLogErrorMomentBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<ErrorLogDataModel> findByOrderId(UUID orderId, Pageable pageable);
    Page<ErrorLogDataModel> findByLogLevel(String logLevel, Pageable pageable);
}