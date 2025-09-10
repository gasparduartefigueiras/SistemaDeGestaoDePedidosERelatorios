package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerNameTest {

    @Test
    void shouldCreateCustomerNameWithValidName() {
        String name = "João Silva";
        CustomerName customerName = new CustomerName(name);

        assertEquals("João Silva", customerName.getCustomerName());
    }

    @Test
    void shouldTrimWhitespaceFromName() {
        CustomerName customerName = new CustomerName("  Maria Santos  ");

        assertEquals("Maria Santos", customerName.getCustomerName());
    }

    @Test
    void shouldAcceptSingleName() {
        CustomerName customerName = new CustomerName("Pedro");

        assertEquals("Pedro", customerName.getCustomerName());
    }

    @Test
    void shouldAcceptNameWithNumbers() {
        CustomerName customerName = new CustomerName("João Silva Jr. 2");

        assertEquals("João Silva Jr. 2", customerName.getCustomerName());
    }

    @Test
    void shouldAcceptNameWithSpecialCharacters() {
        CustomerName customerName = new CustomerName("José D'Almeida-Santos");

        assertEquals("José D'Almeida-Santos", customerName.getCustomerName());
    }

    @Test
    void shouldAcceptNameWithAccents() {
        CustomerName customerName = new CustomerName("António José Pereira");

        assertEquals("António José Pereira", customerName.getCustomerName());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerName(null)
        );

        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerName("")
        );

        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerName("   ")
        );

        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameName() {
        CustomerName name1 = new CustomerName("Ana Silva");
        CustomerName name2 = new CustomerName("Ana Silva");

        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }

    @Test
    void shouldBeEqualWhenNormalizedToSame() {
        CustomerName name1 = new CustomerName("Carlos Pereira");
        CustomerName name2 = new CustomerName("  Carlos Pereira  ");

        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentName() {
        CustomerName name1 = new CustomerName("João Silva");
        CustomerName name2 = new CustomerName("Pedro Santos");

        assertNotEquals(name1, name2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        CustomerName name = new CustomerName("Maria Silva");

        assertNotEquals(name, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        CustomerName name = new CustomerName("Ana Santos");
        String notCustomerName = "Ana Santos";

        assertNotEquals(name, notCustomerName);
    }

    @Test
    void shouldHaveCorrectToStringFormat() {
        CustomerName name = new CustomerName("Carlos Silva");

        assertEquals("CustomerName{Carlos Silva}", name.toString());
    }
}