package SistemaDeGestaoDePedidosERelatorios.assembler;

import SistemaDeGestaoDePedidosERelatorios.DTOs.errorLog.ErrorLogResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.VOs.LogId;
import SistemaDeGestaoDePedidosERelatorios.VOs.LogLevel;
import SistemaDeGestaoDePedidosERelatorios.VOs.LogMessage;
import SistemaDeGestaoDePedidosERelatorios.VOs.LogErrorMoment;
import SistemaDeGestaoDePedidosERelatorios.VOs.OrderId;
import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.ErrorLog;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ErrorLogDTOAssemblerTest {

    @Test
    void toResponseDTOHappyPath() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        LocalDateTime when = LocalDateTime.of(2025, 9, 10, 12, 0);

        ErrorLog log = mock(ErrorLog.class);
        LogId logId = mock(LogId.class);
        OrderId oid = mock(OrderId.class);
        LogLevel lvl = mock(LogLevel.class);
        LogMessage msg = mock(LogMessage.class);
        LogErrorMoment moment = mock(LogErrorMoment.class);

        when(log.getId()).thenReturn(logId);
        when(logId.getLogId()).thenReturn(id);

        when(log.getOrderId()).thenReturn(oid);
        when(oid.getOrderId()).thenReturn(orderId);

        when(log.getLevel()).thenReturn(lvl);
        when(lvl.getLevel()).thenReturn(LogLevel.Level.ERROR);

        when(log.getMessage()).thenReturn(msg);
        when(msg.getMessage()).thenReturn("boom");

        when(log.getErrorMoment()).thenReturn(moment);
        when(moment.getDateTime()).thenReturn(when);

        // Act
        ErrorLogResponseDTO dto = ErrorLogDTOAssembler.toResponseDTO(log);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(orderId, dto.getOrderId());
        assertEquals("ERROR", dto.getLevel());
        assertEquals("boom", dto.getMessage());
        assertEquals(when, dto.getTimestamp());
    }

    @Test
    void toResponseDTONullOrderIdYieldsNullInDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime when = LocalDateTime.now();

        ErrorLog log = mock(ErrorLog.class);
        LogId logId = mock(LogId.class);
        LogLevel lvl = mock(LogLevel.class);
        LogMessage msg = mock(LogMessage.class);
        LogErrorMoment moment = mock(LogErrorMoment.class);

        when(log.getId()).thenReturn(logId);
        when(logId.getLogId()).thenReturn(id);

        when(log.getOrderId()).thenReturn(null); // <- importante

        when(log.getLevel()).thenReturn(lvl);
        when(lvl.getLevel()).thenReturn(LogLevel.Level.WARN);

        when(log.getMessage()).thenReturn(msg);
        when(msg.getMessage()).thenReturn("warn msg");

        when(log.getErrorMoment()).thenReturn(moment);
        when(moment.getDateTime()).thenReturn(when);

        // Act
        ErrorLogResponseDTO dto = ErrorLogDTOAssembler.toResponseDTO(log);

        // Assert
        assertNull(dto.getOrderId());
        assertEquals("WARN", dto.getLevel());
        assertEquals("warn msg", dto.getMessage());
        assertEquals(when, dto.getTimestamp());
    }

    @Test
    void toResponseDTONullLevelObjectYieldsNullLevel() {
        // Arrange
        UUID id = UUID.randomUUID();

        ErrorLog log = mock(ErrorLog.class);
        LogId logId = mock(LogId.class);
        LogMessage msg = mock(LogMessage.class);
        LogErrorMoment moment = mock(LogErrorMoment.class);

        when(log.getId()).thenReturn(logId);
        when(logId.getLogId()).thenReturn(id);

        when(log.getOrderId()).thenReturn(null);
        when(log.getLevel()).thenReturn(null); // <- nível todo nulo
        when(log.getMessage()).thenReturn(msg);
        when(msg.getMessage()).thenReturn("no level");
        when(log.getErrorMoment()).thenReturn(moment);
        when(moment.getDateTime()).thenReturn(LocalDateTime.MIN);

        // Act
        ErrorLogResponseDTO dto = ErrorLogDTOAssembler.toResponseDTO(log);

        // Assert
        assertNull(dto.getLevel());
        assertEquals("no level", dto.getMessage());
    }

    @Test
    void toResponseDTONullEnumInsideLevelYieldsNullLevel() {
        // Arrange
        UUID id = UUID.randomUUID();

        ErrorLog log = mock(ErrorLog.class);
        LogId logId = mock(LogId.class);
        LogLevel lvl = mock(LogLevel.class);
        LogMessage msg = mock(LogMessage.class);
        LogErrorMoment moment = mock(LogErrorMoment.class);

        when(log.getId()).thenReturn(logId);
        when(logId.getLogId()).thenReturn(id);

        when(log.getOrderId()).thenReturn(null);
        when(log.getLevel()).thenReturn(lvl);
        when(lvl.getLevel()).thenReturn(null); // <- enum interno nulo
        when(log.getMessage()).thenReturn(msg);
        when(msg.getMessage()).thenReturn("enum null");
        when(log.getErrorMoment()).thenReturn(moment);
        when(moment.getDateTime()).thenReturn(LocalDateTime.MAX);

        // Act
        ErrorLogResponseDTO dto = ErrorLogDTOAssembler.toResponseDTO(log);

        // Assert
        assertNull(dto.getLevel());
        assertEquals("enum null", dto.getMessage());
    }

    @Test
    void toResponseDTONullMessageYieldsNullMessage() {
        // Arrange
        UUID id = UUID.randomUUID();

        ErrorLog log = mock(ErrorLog.class);
        LogId logId = mock(LogId.class);
        LogLevel lvl = mock(LogLevel.class);
        LogErrorMoment moment = mock(LogErrorMoment.class);

        when(log.getId()).thenReturn(logId);
        when(logId.getLogId()).thenReturn(id);

        when(log.getOrderId()).thenReturn(null);
        when(log.getLevel()).thenReturn(lvl);
        when(lvl.getLevel()).thenReturn(LogLevel.Level.INFO);
        when(log.getMessage()).thenReturn(null); // <- sem mensagem
        when(log.getErrorMoment()).thenReturn(moment);
        when(moment.getDateTime()).thenReturn(LocalDateTime.now());

        // Act
        ErrorLogResponseDTO dto = ErrorLogDTOAssembler.toResponseDTO(log);

        // Assert
        assertEquals("INFO", dto.getLevel());
        assertNull(dto.getMessage());
    }

    @Test
    void toResponseDTONullErrorMomentYieldsNullTimestamp() {
        // Arrange
        UUID id = UUID.randomUUID();

        ErrorLog log = mock(ErrorLog.class);
        LogId logId = mock(LogId.class);
        LogLevel lvl = mock(LogLevel.class);
        LogMessage msg = mock(LogMessage.class);

        when(log.getId()).thenReturn(logId);
        when(logId.getLogId()).thenReturn(id);

        when(log.getLevel()).thenReturn(lvl);
        when(lvl.getLevel()).thenReturn(LogLevel.Level.ERROR);
        when(log.getMessage()).thenReturn(msg);
        when(msg.getMessage()).thenReturn("x");
        when(log.getErrorMoment()).thenReturn(null); // <- sem data

        // Act
        ErrorLogResponseDTO dto = ErrorLogDTOAssembler.toResponseDTO(log);

        // Assert
        assertNull(dto.getTimestamp());
        assertEquals("ERROR", dto.getLevel());
        assertEquals("x", dto.getMessage());
    }
}