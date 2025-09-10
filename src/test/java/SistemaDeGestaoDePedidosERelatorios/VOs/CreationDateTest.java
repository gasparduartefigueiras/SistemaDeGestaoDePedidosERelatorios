package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class CreationDateTest {

    @Test
    void shouldCreateCreationDateWithCurrentDate() {
        LocalDate before = LocalDate.now();
        CreationDate creationDate = new CreationDate();
        LocalDate after = LocalDate.now();

        LocalDate actualDate = creationDate.getCreationDate();
        assertTrue(actualDate.equals(before) || actualDate.equals(after));
    }

    @Test
    void shouldCreateCreationDateWithSpecificDate() {
        LocalDate specificDate = LocalDate.of(2025, 9, 1);
        CreationDate creationDate = new CreationDate(specificDate);

        assertEquals(specificDate, creationDate.getCreationDate());
    }

    @Test
    void shouldAcceptPastDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 15);
        CreationDate creationDate = new CreationDate(pastDate);

        assertEquals(pastDate, creationDate.getCreationDate());
    }

    @Test
    void shouldAcceptFutureDate() {
        LocalDate futureDate = LocalDate.of(2030, 12, 25);
        CreationDate creationDate = new CreationDate(futureDate);

        assertEquals(futureDate, creationDate.getCreationDate());
    }

    @Test
    void shouldThrowExceptionWhenDateIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CreationDate(null)
        );

        assertEquals("DateTime cannot be null.", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameDate() {
        LocalDate date = LocalDate.of(2025, 9, 1);
        CreationDate date1 = new CreationDate(date);
        CreationDate date2 = new CreationDate(date);

        assertEquals(date1, date2);
        assertEquals(date1.hashCode(), date2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentDate() {
        CreationDate date1 = new CreationDate(LocalDate.of(2025, 9, 1));
        CreationDate date2 = new CreationDate(LocalDate.of(2025, 9, 2));

        assertNotEquals(date1, date2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        CreationDate date = new CreationDate(LocalDate.of(2025, 9, 1));

        assertNotEquals(date, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        CreationDate date = new CreationDate(LocalDate.of(2025, 9, 1));
        LocalDate notCreationDate = LocalDate.of(2025, 9, 1);

        assertNotEquals(date, notCreationDate);
    }

    @Test
    void shouldHaveCorrectToStringFormat() {
        LocalDate date = LocalDate.of(2025, 9, 1);
        CreationDate creationDate = new CreationDate(date);

        assertEquals("CreationDate{2025-09-01}", creationDate.toString());
    }
}