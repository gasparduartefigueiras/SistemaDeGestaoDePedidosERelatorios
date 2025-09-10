package SistemaDeGestaoDePedidosERelatorios.domain.errorLog;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import org.springframework.stereotype.Component;

@Component
public class ErrorLogFactory implements IErrorLogFactory {

    @Override
    public ErrorLog createErrorLog(LogLevel level, LogMessage message, OrderId orderId) {
        if (level == null || message == null) {
            throw new IllegalArgumentException("LogLevel and LogMessage cannot be null.");
        }
        LogId id = new LogId();
        LogErrorMoment moment = new LogErrorMoment();
        return new ErrorLog(id, level, message, moment, orderId);
    }

    @Override
    public ErrorLog recreateErrorLog(LogId id, LogLevel level, LogMessage message, LogErrorMoment moment, OrderId orderId) {
        return new ErrorLog(id, level, message, moment, orderId);
    }
}