package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderValueTest {

    @Test
    void shouldCreateOrderValueWithValidAmount() {
        double amount = 100.50;
        OrderValue orderValue = new OrderValue(amount);

        assertEquals(100.50, orderValue.getOrderValue());
    }

    @Test
    void shouldRoundAmountToTwoDecimalPlaces() {
        OrderValue orderValue = new OrderValue(100.555);

        assertEquals(100.56, orderValue.getOrderValue(), 0.001);
    }

    @Test
    void shouldRoundDownWhenLessThanHalf() {
        OrderValue orderValue = new OrderValue(100.554);

        assertEquals(100.55, orderValue.getOrderValue(), 0.001);
    }

    @Test
    void shouldAcceptZeroAmount() {
        OrderValue orderValue = new OrderValue(0.0);

        assertEquals(0.0, orderValue.getOrderValue());
    }

    @Test
    void shouldAcceptVerySmallAmount() {
        OrderValue orderValue = new OrderValue(0.01);

        assertEquals(0.01, orderValue.getOrderValue());
    }

    @Test
    void shouldAcceptLargeAmount() {
        OrderValue orderValue = new OrderValue(999999.99);

        assertEquals(999999.99, orderValue.getOrderValue());
    }

    @Test
    void shouldHandleWholeNumbers() {
        OrderValue orderValue = new OrderValue(100.0);

        assertEquals(100.0, orderValue.getOrderValue());
    }

    @Test
    void shouldHandleOneDecimalPlace() {
        OrderValue orderValue = new OrderValue(50.5);

        assertEquals(50.5, orderValue.getOrderValue());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderValue(-10.0)
        );

        assertEquals("Amount cannot be negative.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegativeSmall() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderValue(-0.01)
        );

        assertEquals("Amount cannot be negative.", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameAmount() {
        OrderValue value1 = new OrderValue(100.50);
        OrderValue value2 = new OrderValue(100.50);

        assertEquals(value1, value2);
        assertEquals(value1.hashCode(), value2.hashCode());
    }

    @Test
    void shouldBeEqualWhenRoundedToSame() {
        OrderValue value1 = new OrderValue(100.555);
        OrderValue value2 = new OrderValue(100.556);

        assertEquals(value1, value2);
        assertEquals(value1.hashCode(), value2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentAmount() {
        OrderValue value1 = new OrderValue(100.50);
        OrderValue value2 = new OrderValue(200.50);

        assertNotEquals(value1, value2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        OrderValue value = new OrderValue(100.50);

        assertNotEquals(value, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        OrderValue value = new OrderValue(100.50);
        Double notOrderValue = 100.50;

        assertNotEquals(value, notOrderValue);
    }

    @Test
    void shouldHaveCorrectToStringFormat() {
        OrderValue value = new OrderValue(100.5);

        assertEquals("OrderValue: 100,50", value.toString());
    }

    @Test
    void shouldHaveCorrectToStringFormatWithWholeNumber() {
        OrderValue value = new OrderValue(100.0);

        assertEquals("OrderValue: 100,00", value.toString());
    }

    @Test
    void shouldHaveCorrectToStringFormatWithRounding() {
        OrderValue value = new OrderValue(100.556);

        assertEquals("OrderValue: 100,56", value.toString());
    }
}