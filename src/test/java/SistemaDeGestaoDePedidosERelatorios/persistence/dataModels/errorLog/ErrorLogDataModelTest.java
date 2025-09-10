package SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.errorLog;

import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ErrorLogDataModelTest {

    @Test
    void jpaAnnotationsAreCorrect() throws NoSuchFieldException {
        assertNotNull(ErrorLogDataModel.class.getAnnotation(Entity.class), "@Entity em falta");

        Table table = ErrorLogDataModel.class.getAnnotation(Table.class);
        assertNotNull(table, "@Table em falta");
        assertEquals("ErrorLog", table.name(), "Nome da tabela incorreto");

        // id
        Field id = ErrorLogDataModel.class.getDeclaredField("id");
        assertNotNull(id.getAnnotation(Id.class), "@Id em falta");
        Column idCol = id.getAnnotation(Column.class);
        assertNotNull(idCol, "@Column em falta no id");
        assertEquals("id", idCol.name());
        assertFalse(idCol.nullable());

        // orderId (nullable)
        Field orderId = ErrorLogDataModel.class.getDeclaredField("orderId");
        Column orderIdCol = orderId.getAnnotation(Column.class);
        assertNotNull(orderIdCol, "@Column em falta no orderId");
        assertEquals("orderId", orderIdCol.name());

        // logLevel
        Field logLevel = ErrorLogDataModel.class.getDeclaredField("logLevel");
        Column logLevelCol = logLevel.getAnnotation(Column.class);
        assertNotNull(logLevelCol, "@Column em falta no logLevel");
        assertEquals("logLevel", logLevelCol.name());
        assertFalse(logLevelCol.nullable());

        // logMessage
        Field logMessage = ErrorLogDataModel.class.getDeclaredField("logMessage");
        Column logMessageCol = logMessage.getAnnotation(Column.class);
        assertNotNull(logMessageCol, "@Column em falta no logMessage");
        assertEquals("logMessage", logMessageCol.name());
        assertFalse(logMessageCol.nullable());

        // logErrorMoment
        Field logErrorMoment = ErrorLogDataModel.class.getDeclaredField("logErrorMoment");
        Column logErrorMomentCol = logErrorMoment.getAnnotation(Column.class);
        assertNotNull(logErrorMomentCol, "@Column em falta no logErrorMoment");
        assertEquals("logErrorMoment", logErrorMomentCol.name());
        assertFalse(logErrorMomentCol.nullable());
    }

    @Test
    void gettersReturnConstructorValues() {
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String level = "ERROR";
        String message = "Erro ao chamar serviço externo";
        LocalDateTime when = LocalDateTime.of(2025, 1, 2, 11, 45);

        ErrorLogDataModel dm = new ErrorLogDataModel(id, orderId, level, message, when);

        assertEquals(id, dm.getId());
        assertEquals(orderId, dm.getOrderId());
        assertEquals(level, dm.getLogLevel());
        assertEquals(message, dm.getLogMessage());
        assertEquals(when, dm.getLogErrorMoment());
    }

    @Test
    void equalsReturnsTrueForSameId() {
        UUID id = UUID.randomUUID();
        ErrorLogDataModel a = new ErrorLogDataModel(id, UUID.randomUUID(), "WARN", "A", LocalDateTime.now());
        ErrorLogDataModel b = new ErrorLogDataModel(id, null, "ERROR", "B", LocalDateTime.now().minusMinutes(10));

        assertEquals(a, b);
        assertEquals(b, a);
        assertEquals(a, a);
    }

    @Test
    void equalsReturnsFalseForDifferentIds() {
        ErrorLogDataModel a = new ErrorLogDataModel(UUID.randomUUID(), null, "INFO", "A", LocalDateTime.now());
        ErrorLogDataModel b = new ErrorLogDataModel(UUID.randomUUID(), null, "INFO", "A", LocalDateTime.now());

        assertNotEquals(a, b);
    }

    @Test
    void equalsReturnsFalseIfAnyIdIsNull() {
        ErrorLogDataModel a = new ErrorLogDataModel(UUID.randomUUID(), null, "ERROR", "X", LocalDateTime.now());
        ErrorLogDataModel b = new ErrorLogDataModel(UUID.randomUUID(), null, "ERROR", "Y", LocalDateTime.now());

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void hashCodeUsesId() {
        UUID id = UUID.randomUUID();
        ErrorLogDataModel a = new ErrorLogDataModel(id, null, "ERROR", "X", LocalDateTime.now());
        ErrorLogDataModel b = new ErrorLogDataModel(id, UUID.randomUUID(), "WARN", "Y", LocalDateTime.now());

        assertEquals(a.hashCode(), b.hashCode(), "hashCode deve basear-se no id");
    }

    @Test
    void toStringContainsTypeIdOrderIdAndLoggedAtOnly() {
        UUID id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        UUID orderId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        LocalDateTime when = LocalDateTime.of(2025, 1, 2, 9, 0);

        ErrorLogDataModel dm = new ErrorLogDataModel(id, orderId, "WARN", "Falhou", when);

        String s = dm.toString();
        assertTrue(s.contains("ErrorLogDataModel"));
        assertTrue(s.contains(id.toString()));
        assertTrue(s.contains(orderId.toString()));
        assertTrue(s.contains("logErrorMoment"));
        assertFalse(s.contains("Falhou"), "toString não deve expor a mensagem do erro");
    }

    @Test
    void protectedNoArgsConstructorExistsForJpa() {
        try {
            Constructor<ErrorLogDataModel> ctor = ErrorLogDataModel.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            ErrorLogDataModel dm = ctor.newInstance();
            assertNotNull(dm);
        } catch (ReflectiveOperationException e) {
            fail("Construtor sem argumentos protegido deve existir para JPA");
        }
    }
}