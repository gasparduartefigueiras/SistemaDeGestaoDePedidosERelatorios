package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.errorLog;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.ErrorLog;
import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.IErrorLogFactory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.errorLog.ErrorLogDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ErrorLogMapperTest {

    private IErrorLogFactory factory;
    private ErrorLogMapper mapper;

    @BeforeEach
    void setup() {
        factory = mock(IErrorLogFactory.class);
        mapper = new ErrorLogMapper(factory);
    }

    @Test
    void constructorRequiresFactory() {
        assertThrows(NullPointerException.class, () -> new ErrorLogMapper(null));
    }

    @Test
    void toDataModelHappyPathWithOrderId() {
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String messageStr = "Falha externa";
        LocalDateTime when = LocalDateTime.of(2025, 1, 2, 10, 0);

        ErrorLog entity = mock(ErrorLog.class);
        LogId logId = mock(LogId.class);
        OrderId oId = mock(OrderId.class);
        LogLevel level = mock(LogLevel.class);
        LogMessage msg = mock(LogMessage.class);
        LogErrorMoment moment = mock(LogErrorMoment.class);

        when(entity.getId()).thenReturn(logId);
        when(entity.getOrderId()).thenReturn(oId);
        when(entity.getLevel()).thenReturn(level);
        when(entity.getMessage()).thenReturn(msg);
        when(entity.getErrorMoment()).thenReturn(moment);

        when(logId.getLogId()).thenReturn(id);
        when(oId.getOrderId()).thenReturn(orderId);

        // ← IMPORTANTE: stub do enum
        when(level.getLevel()).thenReturn(LogLevel.Level.ERROR);

        when(msg.getMessage()).thenReturn(messageStr);
        when(moment.getDateTime()).thenReturn(when);

        ErrorLogDataModel dm = mapper.toDataModel(entity);

        assertEquals(id, dm.getId());
        assertEquals(orderId, dm.getOrderId());
        assertEquals("ERROR", dm.getLogLevel());   // name() do enum
        assertEquals(messageStr, dm.getLogMessage());
        assertEquals(when, dm.getLogErrorMoment());

        verifyNoInteractions(factory);
    }

    @Test
    void toDataModelHappyPathWithNullOrderId() {
        UUID id = UUID.randomUUID();
        String messageStr = "Aviso";
        LocalDateTime when = LocalDateTime.now();

        ErrorLog entity = mock(ErrorLog.class);
        LogId logId = mock(LogId.class);
        LogLevel level = mock(LogLevel.class);
        LogMessage msg = mock(LogMessage.class);
        LogErrorMoment moment = mock(LogErrorMoment.class);

        when(entity.getId()).thenReturn(logId);
        when(entity.getOrderId()).thenReturn(null);
        when(entity.getLevel()).thenReturn(level);
        when(entity.getMessage()).thenReturn(msg);
        when(entity.getErrorMoment()).thenReturn(moment);

        when(logId.getLogId()).thenReturn(id);

        // ← IMPORTANTE: stub do enum
        when(level.getLevel()).thenReturn(LogLevel.Level.WARN);

        when(msg.getMessage()).thenReturn(messageStr);
        when(moment.getDateTime()).thenReturn(when);

        ErrorLogDataModel dm = mapper.toDataModel(entity);

        assertEquals(id, dm.getId());
        assertNull(dm.getOrderId());
        assertEquals("WARN", dm.getLogLevel());
        assertEquals(messageStr, dm.getLogMessage());
        assertEquals(when, dm.getLogErrorMoment());

        verifyNoInteractions(factory);
    }

    @Test
    void toDataModelThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> mapper.toDataModel(null));
        verifyNoInteractions(factory);
    }

    @Test
    void toDomainHappyPathWithOrderId() {
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String levelStr = "ERROR";
        String messageStr = "Falhou";
        LocalDateTime when = LocalDateTime.of(2025, 1, 3, 9, 30);

        ErrorLogDataModel dm = new ErrorLogDataModel(id, orderId, levelStr, messageStr, when);

        ErrorLog expected = mock(ErrorLog.class);
        when(factory.recreateErrorLog(any(LogId.class),
                any(LogLevel.class),
                any(LogMessage.class),
                any(LogErrorMoment.class),
                any(OrderId.class)))
                .thenReturn(expected);

        ErrorLog result = mapper.toDomain(dm);

        assertSame(expected, result);
        verify(factory, times(1)).recreateErrorLog(any(LogId.class),
                any(LogLevel.class),
                any(LogMessage.class),
                any(LogErrorMoment.class),
                any(OrderId.class));
    }

    @Test
    void toDomainThrowsIllegalArgumentOnNull() {
        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(null));
        verifyNoInteractions(factory);
    }

    @Test
    void toDomainWrapsFactoryExceptions() {
        ErrorLogDataModel dm = new ErrorLogDataModel(
                UUID.randomUUID(), null, "ERROR", "msg", LocalDateTime.now()
        );

        when(factory.recreateErrorLog(any(), any(), any(), any(), any()))
                .thenThrow(new IllegalStateException("falha interna"));

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(dm));
        assertTrue(ex.getMessage().contains("ErrorLogDataModel"));
        assertTrue(ex.getCause() instanceof IllegalStateException);
    }
}