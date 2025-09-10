package SistemaDeGestaoDePedidosERelatorios.service.errorLog;

import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.ErrorLog;

import java.util.List;
import java.util.UUID;

public interface IErrorLogService {
    ErrorLog error(String message, UUID orderId);
    ErrorLog warn(String message, UUID orderId);
    ErrorLog info(String message, UUID orderId);
    ErrorLog exception(String message, UUID orderId, Throwable ex);

    List<ErrorLog> getAll();
    List<ErrorLog> getByOrderId(UUID orderId);
    List<ErrorLog> getByLevel(String level);
}