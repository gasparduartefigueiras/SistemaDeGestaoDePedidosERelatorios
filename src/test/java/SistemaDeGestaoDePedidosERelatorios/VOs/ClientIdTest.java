package SistemaDeGestaoDePedidosERelatorios.VOs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class ClientIdTest {

    @Test
    void shouldCreateClientIdWithRandomUUID() {
        ClientId clientId = new ClientId();

        assertNotNull(clientId.getClientId());
        assertTrue(clientId.getClientId() instanceof UUID);
    }

    @Test
    void shouldCreateClientIdWithSpecificUUID() {
        UUID specificId = UUID.randomUUID();
        ClientId clientId = new ClientId(specificId);

        assertEquals(specificId, clientId.getClientId());
    }

    @Test
    void shouldThrowExceptionWhenUUIDIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ClientId(null)
        );

        assertEquals("ClientId cannot be null.", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameUUID() {
        UUID uuid = UUID.randomUUID();
        ClientId clientId1 = new ClientId(uuid);
        ClientId clientId2 = new ClientId(uuid);

        assertEquals(clientId1, clientId2);
        assertEquals(clientId1.hashCode(), clientId2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentUUID() {
        ClientId clientId1 = new ClientId();
        ClientId clientId2 = new ClientId();

        assertNotEquals(clientId1, clientId2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        ClientId clientId = new ClientId();

        assertNotEquals(clientId, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        ClientId clientId = new ClientId();
        String notClientId = "not-a-client-id";

        assertNotEquals(clientId, notClientId);
    }

    @Test
    void shouldHaveCorrectToStringFormat() {
        UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ClientId clientId = new ClientId(uuid);

        assertEquals("ClientId{550e8400-e29b-41d4-a716-446655440000}", clientId.toString());
    }

    @Test
    void shouldGenerateUniqueIds() {
        ClientId clientId1 = new ClientId();
        ClientId clientId2 = new ClientId();
        ClientId clientId3 = new ClientId();

        assertNotEquals(clientId1.getClientId(), clientId2.getClientId());
        assertNotEquals(clientId2.getClientId(), clientId3.getClientId());
        assertNotEquals(clientId1.getClientId(), clientId3.getClientId());
    }
}