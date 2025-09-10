package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationMessageTest {

    @Test
    void shouldCreateValidationMessageWithValidMessage() {
        String message = "Client validation successful";
        ValidationMessage validationMessage = new ValidationMessage(message);

        assertEquals("Client validation successful", validationMessage.getValidationMessage());
    }

    @Test
    void shouldTrimWhitespaceFromMessage() {
        ValidationMessage validationMessage = new ValidationMessage("  SUCCESS  ");

        assertEquals("SUCCESS", validationMessage.getValidationMessage());
    }

    @Test
    void shouldCreateSuccessMessageWithCustomText() {
        ValidationMessage validationMessage = ValidationMessage.success("Client OK");

        assertEquals("Client OK", validationMessage.getValidationMessage());
    }

    @Test
    void shouldCreateSuccessMessageWithDefaultWhenNull() {
        ValidationMessage validationMessage = ValidationMessage.success(null);

        assertEquals("SUCCESS", validationMessage.getValidationMessage());
    }

    @Test
    void shouldCreateErrorMessageUsingFactoryMethod() {
        ValidationMessage validationMessage = ValidationMessage.error("Client not found");

        assertEquals("ERROR: Client not found", validationMessage.getValidationMessage());
    }

    @Test
    void shouldAcceptLongMessage() {
        String longMessage = "This is a very long validation message that contains detailed information about the validation process";
        ValidationMessage validationMessage = new ValidationMessage(longMessage);

        assertEquals(longMessage, validationMessage.getValidationMessage());
    }

    @Test
    void shouldThrowExceptionWhenMessageIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ValidationMessage(null)
        );

        assertEquals("Validation message cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMessageIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ValidationMessage("")
        );

        assertEquals("Validation message cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMessageIsOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ValidationMessage("   ")
        );

        assertEquals("Validation message cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameMessage() {
        ValidationMessage message1 = new ValidationMessage("SUCCESS");
        ValidationMessage message2 = new ValidationMessage("SUCCESS");

        assertEquals(message1, message2);
        assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    void shouldBeEqualWhenNormalizedToSame() {
        ValidationMessage message1 = new ValidationMessage("SUCCESS");
        ValidationMessage message2 = new ValidationMessage("  SUCCESS  ");

        assertEquals(message1, message2);
        assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentMessage() {
        ValidationMessage message1 = new ValidationMessage("SUCCESS");
        ValidationMessage message2 = new ValidationMessage("ERROR: Failed");

        assertNotEquals(message1, message2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        ValidationMessage message = new ValidationMessage("SUCCESS");

        assertNotEquals(message, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        ValidationMessage message = new ValidationMessage("SUCCESS");
        String notValidationMessage = "SUCCESS";

        assertNotEquals(message, notValidationMessage);
    }

    @Test
    void shouldHaveCorrectToStringFormat() {
        ValidationMessage message = new ValidationMessage("Client validated successfully");

        assertEquals("ValidationMessage{Client validated successfully}", message.toString());
    }

    @Test
    void shouldHaveCorrectToStringFormatWithFactoryMethod() {
        ValidationMessage message = ValidationMessage.error("Connection timeout");

        assertEquals("ValidationMessage{ERROR: Connection timeout}", message.toString());
    }
}