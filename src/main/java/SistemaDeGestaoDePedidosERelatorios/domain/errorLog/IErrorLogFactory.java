package SistemaDeGestaoDePedidosERelatorios.domain.errorLog;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;

public interface IErrorLogFactory {

    ErrorLog createErrorLog(LogLevel level, LogMessage message, OrderId orderId);

    ErrorLog recreateErrorLog(LogId id, LogLevel level, LogMessage message, LogErrorMoment moment, OrderId orderId);
}