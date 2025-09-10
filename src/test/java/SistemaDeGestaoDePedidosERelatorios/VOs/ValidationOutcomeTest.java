package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationOutcomeTest {

    @Test
    void constructorStoresFields() {
        ValidationOutcome ok = new ValidationOutcome(true, "Tudo válido");
        assertTrue(ok.isApproved());
        assertEquals("Tudo válido", ok.getMessage());

        ValidationOutcome nok = new ValidationOutcome(false, "Email inválido");
        assertFalse(nok.isApproved());
        assertEquals("Email inválido", nok.getMessage());
    }

    @Test
    void nullMessageIsRejected() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> new ValidationOutcome(true, null));
        assertTrue(ex.getMessage().toLowerCase().contains("message"));
    }

    @Test
    void blankMessageIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new ValidationOutcome(true, ""));
        assertThrows(IllegalArgumentException.class, () -> new ValidationOutcome(false, "   "));
    }

    @Test
    void equalsAndHashCodeUseApprovedAndMessage() {
        ValidationOutcome a = new ValidationOutcome(true, "OK");
        ValidationOutcome b = new ValidationOutcome(true, "OK");
        ValidationOutcome c = new ValidationOutcome(false, "OK");
        ValidationOutcome d = new ValidationOutcome(true, "Diferente");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
        assertNotEquals(a, d);
        assertNotEquals(c, d);
    }

    @Test
    void toStringContainsKeyInfo() {
        ValidationOutcome v = new ValidationOutcome(false, "Motivo X");
        String s = v.toString();
        assertTrue(s.contains("approved"));
        assertTrue(s.contains("false"));
        assertTrue(s.contains("Motivo X"));
    }
}