package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerEmailTest {

    @Test
    void shouldCreateCustomerEmailWithValidEmail() {
        String email = "user@example.com";
        CustomerEmail customerEmail = new CustomerEmail(email);

        assertEquals("user@example.com", customerEmail.getEmail());
    }

    @Test
    void shouldNormalizeEmailToLowercase() {
        CustomerEmail customerEmail = new CustomerEmail("USER@EXAMPLE.COM");

        assertEquals("user@example.com", customerEmail.getEmail());
    }

    @Test
    void shouldTrimWhitespaceFromEmail() {
        CustomerEmail customerEmail = new CustomerEmail("  user@example.com  ");

        assertEquals("user@example.com", customerEmail.getEmail());
    }

    @Test
    void shouldAcceptEmailWithNumbers() {
        CustomerEmail customerEmail = new CustomerEmail("user123@example123.com");

        assertEquals("user123@example123.com", customerEmail.getEmail());
    }

    @Test
    void shouldAcceptEmailWithSpecialCharacters() {
        CustomerEmail customerEmail = new CustomerEmail("user.name+tag@example.com");

        assertEquals("user.name+tag@example.com", customerEmail.getEmail());
    }

    @Test
    void shouldAcceptEmailWithHyphenInDomain() {
        CustomerEmail customerEmail = new CustomerEmail("user@sub-domain.example.com");

        assertEquals("user@sub-domain.example.com", customerEmail.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerEmail(null)
        );

        assertEquals("Email cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerEmail("")
        );

        assertEquals("Email cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerEmail("   ")
        );

        assertEquals("Email cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailHasNoAtSymbol() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerEmail("userexample.com")
        );

        assertEquals("Invalid email format: userexample.com", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailHasNoDomain() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerEmail("user@")
        );

        assertEquals("Invalid email format: user@", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailHasNoLocalPart() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerEmail("@example.com")
        );

        assertEquals("Invalid email format: @example.com", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailHasInvalidDomainExtension() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerEmail("user@example.c")
        );

        assertEquals("Invalid email format: user@example.c", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailHasMultipleAtSymbols() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerEmail("user@@example.com")
        );

        assertEquals("Invalid email format: user@@example.com", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameEmail() {
        CustomerEmail email1 = new CustomerEmail("user@example.com");
        CustomerEmail email2 = new CustomerEmail("user@example.com");

        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void shouldBeEqualWhenNormalizedToSame() {
        CustomerEmail email1 = new CustomerEmail("USER@EXAMPLE.COM");
        CustomerEmail email2 = new CustomerEmail("  user@example.com  ");

        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentEmail() {
        CustomerEmail email1 = new CustomerEmail("user1@example.com");
        CustomerEmail email2 = new CustomerEmail("user2@example.com");

        assertNotEquals(email1, email2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        CustomerEmail email = new CustomerEmail("user@example.com");

        assertNotEquals(email, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        CustomerEmail email = new CustomerEmail("user@example.com");
        String notEmail = "user@example.com";

        assertNotEquals(email, notEmail);
    }

    @Test
    void shouldHaveCorrectToStringFormat() {
        CustomerEmail email = new CustomerEmail("user@example.com");

        assertEquals("CustomerEmail{user@example.com}", email.toString());
    }
}