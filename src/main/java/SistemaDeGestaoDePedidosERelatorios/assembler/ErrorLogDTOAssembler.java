package SistemaDeGestaoDePedidosERelatorios.assembler;

import SistemaDeGestaoDePedidosERelatorios.DTOs.errorLog.ErrorLogResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.ErrorLog;

import java.util.UUID;

public class ErrorLogDTOAssembler {

    private ErrorLogDTOAssembler() {
    }

    public static ErrorLogResponseDTO toResponseDTO(ErrorLog log) {
        UUID orderId = null;
        if (log.getOrderId() != null) {
            orderId = log.getOrderId().getOrderId();
        }

        String level = null;
        if (log.getLevel() != null && log.getLevel().getLevel() != null) {
            level = log.getLevel().getLevel().name();
        }

        String message = null;
        if (log.getMessage() != null) {
            message = log.getMessage().getMessage();
        }

        java.time.LocalDateTime errorMoment = null;
        if (log.getErrorMoment() != null) {
            errorMoment = log.getErrorMoment().getDateTime();
        }

        return new ErrorLogResponseDTO(
                log.getId().getLogId(),
                orderId,
                level,
                message,
                errorMoment
        );
    }
}