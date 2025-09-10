package SistemaDeGestaoDePedidosERelatorios.domain.errorLog;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import java.time.LocalDateTime;
import java.util.UUID;

class ErrorLogTest {

    private LogId newId() { return new LogId(UUID.randomUUID()); }
    private LogErrorMoment newMoment() { return new LogErrorMoment(LocalDateTime.now()); }
    private OrderId newOrderId() { return new OrderId(UUID.randomUUID()); }

    @Test
    void shouldCreateValidErrorLogWithoutOrder() {
        ErrorLog log = new ErrorLog(newId(), LogLevel.error(), new LogMessage("System failure"), newMoment(), null);

        assertNotNull(log.getId());
        assertEquals(LogLevel.Level.ERROR, log.getLevel().getLevel());
        assertEquals("System failure", log.getMessage().getMessage());
        assertNotNull(log.getErrorMoment().getDateTime());
        assertNull(log.getOrderId());
    }

    @Test
    void shouldCreateValidErrorLogWithOrder() {
        OrderId orderId = newOrderId();
        ErrorLog log = new ErrorLog(newId(), LogLevel.warn(), new LogMessage("Validation warning"), newMoment(), orderId);

        assertEquals(orderId, log.getOrderId());
        assertEquals(LogLevel.Level.WARN, log.getLevel().getLevel());
    }

    @Test
    void shouldThrowIfIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ErrorLog(null, LogLevel.error(), new LogMessage("msg"), newMoment(), null));
    }

    @Test
    void shouldThrowIfLevelIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ErrorLog(newId(), null, new LogMessage("msg"), newMoment(), null));
    }

    @Test
    void shouldThrowIfMessageIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ErrorLog(newId(), LogLevel.warn(), null, newMoment(), null));
    }

    @Test
    void shouldThrowIfMomentIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ErrorLog(newId(), LogLevel.info(), new LogMessage("msg"), null, null));
    }

    @Test
    void equalsAndHashCodeBasedOnId() {
        LogId id = newId();
        ErrorLog log1 = new ErrorLog(id, LogLevel.error(), new LogMessage("x"), newMoment(), null);
        ErrorLog log2 = new ErrorLog(id, LogLevel.warn(), new LogMessage("y"), newMoment(), newOrderId());

        assertEquals(log1, log2);
        assertEquals(log1.hashCode(), log2.hashCode());
    }

    @Test
    void notEqualsForDifferentIds() {
        ErrorLog log1 = new ErrorLog(newId(), LogLevel.error(), new LogMessage("x"), newMoment(), null);
        ErrorLog log2 = new ErrorLog(newId(), LogLevel.error(), new LogMessage("x"), newMoment(), null);

        assertNotEquals(log1, log2);
    }

    @Test
    void toStringContainsAllFields() {
        ErrorLog log = new ErrorLog(
                newId(),
                LogLevel.warn(),
                new LogMessage("disk full"),
                new LogErrorMoment(LocalDateTime.of(2025,1,1,10,0)),
                newOrderId()
        );
        String s = log.toString();

        assertTrue(s.contains("ErrorLog"));
        assertTrue(s.contains("WARN"));
        assertTrue(s.contains("disk full"));
        assertTrue(s.contains("2025-01-01T10:00"));
        assertTrue(s.contains("orderId")); // campo presente no toString
    }
}