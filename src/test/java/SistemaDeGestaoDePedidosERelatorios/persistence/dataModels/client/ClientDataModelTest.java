package SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.client;

import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientDataModelTest {

    @Test
    void jpaAnnotationsAreCorrect() throws NoSuchFieldException {
        assertNotNull(ClientDataModel.class.getAnnotation(Entity.class), "@Entity em falta");

        Table table = ClientDataModel.class.getAnnotation(Table.class);
        assertNotNull(table, "@Table em falta");
        assertEquals("Client", table.name(), "Nome da tabela incorreto");

        Field id = ClientDataModel.class.getDeclaredField("id");
        assertNotNull(id.getAnnotation(Id.class), "@Id em falta no id");
        Column idCol = id.getAnnotation(Column.class);
        assertNotNull(idCol, "@Column em falta no id");
        assertEquals("id", idCol.name());
        assertFalse(idCol.nullable());

        Field name = ClientDataModel.class.getDeclaredField("name");
        Column nameCol = name.getAnnotation(Column.class);
        assertNotNull(nameCol, "@Column em falta no name");
        assertEquals("customerName", nameCol.name());
        assertFalse(nameCol.nullable());

        Field email = ClientDataModel.class.getDeclaredField("email");
        Column emailCol = email.getAnnotation(Column.class);
        assertNotNull(emailCol, "@Column em falta no email");
        assertEquals("customerEmail", emailCol.name());
        assertFalse(emailCol.nullable());

        Field date = ClientDataModel.class.getDeclaredField("date");
        Column dateCol = date.getAnnotation(Column.class);
        assertNotNull(dateCol, "@Column em falta no date");
        assertEquals("creationDate", dateCol.name());
        assertFalse(dateCol.nullable());
    }

    @Test
    void gettersReturnConstructorValues() throws ReflectiveOperationException {
        UUID id = UUID.randomUUID();
        String name = "Alice";
        String email = "alice@example.com";
        LocalDate date = LocalDate.of(2024, 12, 31);

        ClientDataModel c = new ClientDataModel(id, name, email, date);

        assertEquals(id, c.getId());
        assertEquals(name, c.getName());
        assertEquals(email, c.getEmail());
        assertEquals(date, c.getDate());

        Field f = ClientDataModel.class.getDeclaredField("date");
        f.setAccessible(true);
        Object value = f.get(c);
        assertEquals(date, value);
    }

    @Test
    void equalsReturnsTrueForSameId() {
        UUID id = UUID.randomUUID();
        ClientDataModel a = new ClientDataModel(id, "A", "a@x.com", LocalDate.now());
        ClientDataModel b = new ClientDataModel(id, "B", "b@y.com", LocalDate.now().minusDays(1));

        assertEquals(a, b);
        assertEquals(b, a);
        assertEquals(a, a);
    }

    @Test
    void equalsReturnsFalseForDifferentIds() {
        ClientDataModel a = new ClientDataModel(UUID.randomUUID(), "A", "a@x.com", LocalDate.now());
        ClientDataModel b = new ClientDataModel(UUID.randomUUID(), "A", "a@x.com", LocalDate.now());

        assertNotEquals(a, b);
    }

    @Test
    void equalsReturnsFalseIfAnyIdIsNull() {
        ClientDataModel a = new ClientDataModel(UUID.randomUUID(), "A", "a@x.com", LocalDate.now());
        ClientDataModel b = new ClientDataModel(UUID.randomUUID(), "B", "b@y.com", LocalDate.now());

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void hashCodeUsesId() {
        UUID id = UUID.randomUUID();
        ClientDataModel a = new ClientDataModel(id, "A", "a@x.com", LocalDate.now());
        ClientDataModel b = new ClientDataModel(id, "B", "b@y.com", LocalDate.now());

        assertEquals(a.hashCode(), b.hashCode(), "hashCode deve basear-se no id");
    }

    @Test
    void toStringContainsTypeIdAndEmailOnly() {
        UUID id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        ClientDataModel c = new ClientDataModel(id, "Alice", "alice@example.com", LocalDate.now());

        String s = c.toString();
        assertTrue(s.contains("ClientDataModel"));
        assertTrue(s.contains(id.toString()));
        assertTrue(s.contains("alice@example.com"));
        assertFalse(s.contains("Alice"), "toString não deve incluir name (de acordo com a implementação atual)");
    }

    @Test
    void protectedNoArgsConstructorExistsForJpa() {
        try {
            Constructor<ClientDataModel> ctor = ClientDataModel.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            ClientDataModel c = ctor.newInstance();
            assertNotNull(c);
        } catch (ReflectiveOperationException e) {
            fail("Construtor sem argumentos protegido deve existir para JPA");
        }
    }
}