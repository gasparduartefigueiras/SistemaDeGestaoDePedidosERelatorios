package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.errorLog;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.ErrorLog;
import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.IErrorLogFactory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.errorLog.ErrorLogDataModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Component
public class ErrorLogMapper implements IErrorLogMapper {

    private final IErrorLogFactory errorLogFactory;

    public ErrorLogMapper(IErrorLogFactory errorLogFactory) {
        this.errorLogFactory = Objects.requireNonNull(errorLogFactory, "factory");
    }

    @Override
    public ErrorLogDataModel toDataModel(ErrorLog errorLog) {
        Objects.requireNonNull(errorLog, "errorLog");

        UUID id = errorLog.getId().getLogId();

        UUID orderId;
        if (errorLog.getOrderId() == null) {
            orderId = null;
        } else {
            orderId = errorLog.getOrderId().getOrderId();
        }

        String logLevel = errorLog.getLevel().getLevel().name();

        String message = errorLog.getMessage().getMessage();
        LocalDateTime dateTime = errorLog.getErrorMoment().getDateTime();

        return new ErrorLogDataModel(id, orderId, logLevel, message, dateTime);
    }

    @Override
    public ErrorLog toDomain(ErrorLogDataModel errorLogDataModelDataModel) {
        try {
            Objects.requireNonNull(errorLogDataModelDataModel, "dataModel");

            LogId id = new LogId(errorLogDataModelDataModel.getId());

            OrderId orderId;
            if (errorLogDataModelDataModel.getOrderId() == null) {
                orderId = null;
            } else {
                orderId = new OrderId(errorLogDataModelDataModel.getOrderId());
            }

            LogLevel.Level lvl = LogLevel.Level.valueOf(
                    errorLogDataModelDataModel.getLogLevel().trim().toUpperCase()
            );
            LogLevel logLevel = new LogLevel(lvl);

            LogMessage message = new LogMessage(errorLogDataModelDataModel.getLogMessage());
            LogErrorMoment moment = new LogErrorMoment(errorLogDataModelDataModel.getLogErrorMoment());

            return errorLogFactory.recreateErrorLog(id, logLevel, message, moment, orderId);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error trying to map ErrorLogDataModel to ErrorLog", e);
        }
    }
}
