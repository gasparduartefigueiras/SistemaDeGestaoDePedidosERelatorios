package SistemaDeGestaoDePedidosERelatorios.domain.errorLog;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;

class ErrorLogFactoryTest {

    private final IErrorLogFactory factory = new ErrorLogFactory();

    @Test
    void createGeneratesIdMomentAndAllowsNullOrderId() {
        ErrorLog log = factory.createErrorLog(
                LogLevel.error(),
                new LogMessage("System failure"),
                null // sem order
        );

        assertNotNull(log.getId());
        assertNotNull(log.getId().getLogId());
        assertNotNull(log.getErrorMoment());
        assertNotNull(log.getErrorMoment().getDateTime());
        assertEquals(LogLevel.Level.ERROR, log.getLevel().getLevel());
        assertEquals("System failure", log.getMessage().getMessage());
        assertNull(log.getOrderId());
    }

    @Test
    void createWithOrderIdSetsOrderId() {
        OrderId orderId = new OrderId(UUID.randomUUID());

        ErrorLog log = factory.createErrorLog(
                LogLevel.warn(),
                new LogMessage("Validation warning"),
                orderId
        );

        assertEquals(orderId, log.getOrderId());
        assertEquals(LogLevel.Level.WARN, log.getLevel().getLevel());
    }

    @Test
    void createThrowsWhenLevelIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createErrorLog(null, new LogMessage("msg"), null));
    }

    @Test
    void createThrowsWhenMessageIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createErrorLog(LogLevel.warn(), null, null));
    }

    @Test
    void recreateUsesProvidedValuesIncludingOrderId() {
        LogId id = new LogId(UUID.randomUUID());
        LogLevel level = LogLevel.info();
        LogMessage message = new LogMessage("Startup complete");
        LogErrorMoment moment = new LogErrorMoment(LocalDateTime.of(2025, 1, 1, 12, 0, 0));
        OrderId orderId = new OrderId(UUID.randomUUID());

        ErrorLog log = factory.recreateErrorLog(id, level, message, moment, orderId);

        assertEquals(id, log.getId());
        assertEquals(level, log.getLevel());
        assertEquals(message, log.getMessage());
        assertEquals(moment, log.getErrorMoment());
        assertEquals(orderId, log.getOrderId());
    }
}