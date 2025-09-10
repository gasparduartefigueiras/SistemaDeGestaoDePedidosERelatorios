package SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.orderStatusHistory;

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

class OrderStatusHistoryDataModelTest {

    @Test
    void jpaAnnotationsAreCorrect() throws NoSuchFieldException {
        // @Entity
        assertNotNull(OrderStatusHistoryDataModel.class.getAnnotation(Entity.class), "@Entity em falta");

        // @Table(name="OrderStatusHistory")
        Table table = OrderStatusHistoryDataModel.class.getAnnotation(Table.class);
        assertNotNull(table, "@Table em falta");
        assertEquals("OrderStatusHistory", table.name(), "Nome da tabela incorreto");

        // id
        Field id = OrderStatusHistoryDataModel.class.getDeclaredField("id");
        assertNotNull(id.getAnnotation(Id.class), "@Id em falta no id");
        Column idCol = id.getAnnotation(Column.class);
        assertNotNull(idCol, "@Column em falta no id");
        assertEquals("id", idCol.name());
        assertFalse(idCol.nullable());

        // orderId
        Field orderId = OrderStatusHistoryDataModel.class.getDeclaredField("orderId");
        Column orderIdCol = orderId.getAnnotation(Column.class);
        assertNotNull(orderIdCol, "@Column em falta no orderId");
        assertEquals("orderId", orderIdCol.name());
        assertFalse(orderIdCol.nullable());

        // initialStatus
        Field initialStatus = OrderStatusHistoryDataModel.class.getDeclaredField("initialStatus");
        Column initialStatusCol = initialStatus.getAnnotation(Column.class);
        assertNotNull(initialStatusCol, "@Column em falta no initialStatus");
        assertEquals("initialStatus", initialStatusCol.name());
        assertFalse(initialStatusCol.nullable());

        // finalStatus
        Field finalStatus = OrderStatusHistoryDataModel.class.getDeclaredField("finalStatus");
        Column finalStatusCol = finalStatus.getAnnotation(Column.class);
        assertNotNull(finalStatusCol, "@Column em falta no finalStatus");
        assertEquals("finalStatus", finalStatusCol.name());
        assertFalse(finalStatusCol.nullable());

        // reason (nullable)
        Field reason = OrderStatusHistoryDataModel.class.getDeclaredField("reason");
        Column reasonCol = reason.getAnnotation(Column.class);
        assertNotNull(reasonCol, "@Column em falta no reason");
        assertEquals("reason", reasonCol.name());

        // changedBy
        Field changedBy = OrderStatusHistoryDataModel.class.getDeclaredField("changedBy");
        Column changedByCol = changedBy.getAnnotation(Column.class);
        assertNotNull(changedByCol, "@Column em falta no changedBy");
        assertEquals("changedBy", changedByCol.name());
        assertFalse(changedByCol.nullable());

        // changedAt
        Field changedAt = OrderStatusHistoryDataModel.class.getDeclaredField("changedAt");
        Column changedAtCol = changedAt.getAnnotation(Column.class);
        assertNotNull(changedAtCol, "@Column em falta no changedAt");
        assertEquals("changedAt", changedAtCol.name());
        assertFalse(changedAtCol.nullable());
    }

    @Test
    void gettersReturnConstructorValues() {
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        String initialStatus = "PENDENTE";
        String finalStatus = "APROVADO";
        String reason = "Pagamento confirmado";
        String changedBy = "system";
        LocalDateTime changedAt = LocalDateTime.of(2025, 1, 1, 10, 30);

        OrderStatusHistoryDataModel dm = new OrderStatusHistoryDataModel(
                id, orderId, initialStatus, finalStatus, reason, changedBy, changedAt
        );

        assertEquals(id, dm.getId());
        assertEquals(orderId, dm.getOrderId());
        assertEquals(initialStatus, dm.getInitialStatus());
        assertEquals(finalStatus, dm.getFinalStatus());
        assertEquals(reason, dm.getReason());
        assertEquals(changedBy, dm.getChangedBy());
        assertEquals(changedAt, dm.getChangedAt());
    }

    @Test
    void equalsReturnsTrueForSameId() {
        UUID id = UUID.randomUUID();

        OrderStatusHistoryDataModel a = new OrderStatusHistoryDataModel(
                id, UUID.randomUUID(), "PENDENTE", "APROVADO", null, "userA",
                LocalDateTime.now()
        );
        OrderStatusHistoryDataModel b = new OrderStatusHistoryDataModel(
                id, UUID.randomUUID(), "APROVADO", "REJEITADO", "motivo", "userB",
                LocalDateTime.now().minusDays(1)
        );

        assertEquals(a, b);
        assertEquals(b, a);
        assertEquals(a, a);
    }

    @Test
    void equalsReturnsFalseForDifferentIds() {
        OrderStatusHistoryDataModel a = new OrderStatusHistoryDataModel(
                UUID.randomUUID(), UUID.randomUUID(), "PENDENTE", "APROVADO", null, "userA",
                LocalDateTime.now()
        );
        OrderStatusHistoryDataModel b = new OrderStatusHistoryDataModel(
                UUID.randomUUID(), UUID.randomUUID(), "PENDENTE", "APROVADO", null, "userA",
                LocalDateTime.now()
        );

        assertNotEquals(a, b);
    }

    @Test
    void equalsReturnsFalseIfAnyIdIsNull() {
        OrderStatusHistoryDataModel a = new OrderStatusHistoryDataModel(
                UUID.randomUUID(), UUID.randomUUID(), "PENDENTE", "APROVADO", null, "userA",
                LocalDateTime.now()
        );
        OrderStatusHistoryDataModel b = new OrderStatusHistoryDataModel(
                UUID.randomUUID(), UUID.randomUUID(), "PENDENTE", "REJEITADO", "motivo", "userB",
                LocalDateTime.now()
        );

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void hashCodeUsesId() {
        UUID id = UUID.randomUUID();

        OrderStatusHistoryDataModel a = new OrderStatusHistoryDataModel(
                id, UUID.randomUUID(), "PENDENTE", "APROVADO", null, "userA",
                LocalDateTime.now()
        );
        OrderStatusHistoryDataModel b = new OrderStatusHistoryDataModel(
                id, UUID.randomUUID(), "APROVADO", "REJEITADO", "motivo", "userB",
                LocalDateTime.now()
        );

        assertEquals(a.hashCode(), b.hashCode(), "hashCode deve basear-se no id");
    }

    @Test
    void toStringContainsTypeIdOrderIdFinalStatusAndChangedAtOnly() {
        UUID id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        UUID orderId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        OrderStatusHistoryDataModel dm = new OrderStatusHistoryDataModel(
                id, orderId, "PENDENTE", "APROVADO", "ok", "system",
                LocalDateTime.of(2025, 1, 1, 9, 0)
        );

        String s = dm.toString();
        assertTrue(s.contains("OrderStatusHistoryDataModel"));
        assertTrue(s.contains(id.toString()));
        assertTrue(s.contains(orderId.toString()));
        assertTrue(s.contains("APROVADO"));        // finalStatus aparece no toString
        assertFalse(s.contains("PENDENTE"));       // initialStatus não aparece
        assertTrue(s.contains("changedAt"));       // campo está presente
    }

    @Test
    void protectedNoArgsConstructorExistsForJpa() {
        try {
            Constructor<OrderStatusHistoryDataModel> ctor =
                    OrderStatusHistoryDataModel.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            OrderStatusHistoryDataModel dm = ctor.newInstance();
            assertNotNull(dm);
        } catch (ReflectiveOperationException e) {
            fail("Construtor sem argumentos protegido deve existir para JPA");
        }
    }

}