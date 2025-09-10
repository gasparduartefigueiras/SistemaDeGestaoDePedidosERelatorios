package SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderDataModelTest {

    private OrderDataModel newOrder(UUID id) {
        return new OrderDataModel(
                id,
                "Ana Silva",
                "ana@example.com",
                123.45,
                LocalDate.of(2025, 9, 1),
                "PENDING",
                "OK"
        );
    }

    @Nested
    @DisplayName("Construção e Getters")
    class ConstructionAndGetters {

        @Test
        void constructor_sets_all_fields() {
            UUID id = UUID.randomUUID();
            LocalDate date = LocalDate.of(2025, 9, 1);

            OrderDataModel dm = new OrderDataModel(
                    id, "Ana Silva", "ana@example.com",
                    10.50, date, "PENDING", "OK"
            );

            assertEquals(id, dm.getId());
            assertEquals("Ana Silva", dm.getCustomerName());
            assertEquals("ana@example.com", dm.getCustomerEmail());
            assertEquals(10.50, dm.getOrderValue(), 0.000001);
            assertEquals(date, dm.getCreationDate());
            assertEquals("PENDING", dm.getStatus());
            assertEquals("OK", dm.getValidationMessage());
        }

        @Test
        void noargs_constructor_exists_for_jpa() {
            // Apenas garante que o construtor protegido sem args existe e pode ser invocado via reflexão
            assertDoesNotThrow(() -> {
                OrderDataModel dm = OrderDataModel.class.getDeclaredConstructor().newInstance();
                assertNotNull(dm);
            });
        }
    }

    @Nested
    @DisplayName("equals / hashCode")
    class Equality {

        @Test
        void equals_true_when_same_id() {
            UUID id = UUID.randomUUID();
            OrderDataModel a = newOrder(id);
            OrderDataModel b = newOrder(id);

            assertEquals(a, b);
            assertEquals(b, a);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void equals_false_when_different_id() {
            OrderDataModel a = newOrder(UUID.randomUUID());
            OrderDataModel b = newOrder(UUID.randomUUID());

            assertNotEquals(a, b);
            assertNotEquals(b, a);
        }

        @Test
        void equals_false_when_any_id_is_null() {
            OrderDataModel a = newOrder(null);
            OrderDataModel b = newOrder(UUID.randomUUID());
            OrderDataModel c = newOrder(null);

            assertNotEquals(a, b); // this.id == null
            assertNotEquals(b, a); // that.id == null
            assertNotEquals(a, c); // ambos null → não iguais
        }

        @Test
        void equals_reflexive_symmetric_consistent() {
            OrderDataModel a = newOrder(UUID.randomUUID());

            // reflexivo
            assertEquals(a, a);

            // simétrico
            OrderDataModel b = newOrder(a.getId());
            assertEquals(a, b);
            assertEquals(b, a);

            // consistente
            assertEquals(a, b);
            assertEquals(a, b);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringTests {
        @Test
        void toString_contains_key_fields() {
            UUID id = UUID.randomUUID();
            OrderDataModel dm = newOrder(id);
            String s = dm.toString();

            assertTrue(s.contains("OrderDataModel{"));
            assertTrue(s.contains(id.toString()));
            assertTrue(s.contains("ana@example.com"));
            assertTrue(s.contains("PENDING"));
        }
    }

    @Nested
    @DisplayName("Anotações JPA (reflexão)")
    class JpaAnnotations {

        @Test
        void entity_and_table_annotations_present() {
            assertTrue(OrderDataModel.class.isAnnotationPresent(Entity.class), "@Entity em falta");

            Table table = OrderDataModel.class.getAnnotation(Table.class);
            assertNotNull(table, "@Table em falta");
            assertEquals("Orders", table.name(), "Nome da tabela deve ser 'Orders'");
        }

        @Test
        void columns_have_expected_names_and_nullability() throws Exception {
            assertColumn("id", "id", true);
            assertColumn("customerName", "customerName", true);
            assertColumn("customerEmail", "customerEmail", true);
            assertColumn("orderValue", "orderValue", true);
            assertColumn("creationDate", "creationDate", true);
            assertColumn("status", "status", true);
            assertColumn("validationMessage", "validationMessage", false);
        }

        private void assertColumn(String fieldName, String expectedColumnName, boolean expectedNotNull) throws Exception {
            Field f = OrderDataModel.class.getDeclaredField(fieldName);
            Column col = f.getAnnotation(Column.class);
            assertNotNull(col, () -> "@Column em falta para campo " + fieldName);
            assertEquals(expectedColumnName, col.name(), () -> "Nome de coluna inesperado em " + fieldName);
            assertEquals(expectedNotNull, !col.nullable(), () -> "Nullability inesperada em " + fieldName);
        }
    }
}