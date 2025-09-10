package SistemaDeGestaoDePedidosERelatorios.domain.client;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import SistemaDeGestaoDePedidosERelatorios.VOs.*;

class ClientFactoryTest {

    private final IClientFactory factory = new ClientFactory();

    @Test
    void createClient_shouldGenerateIdAndDate() {
        CustomerName name = new CustomerName("Alice");
        CustomerEmail email = new CustomerEmail("alice@example.com");

        Client client = factory.createClient(name, email);

        assertNotNull(client.getId());
        assertNotNull(client.getDate());
        assertEquals(name, client.getName());
        assertEquals(email, client.getEmail());
    }

    @Test
    void createClient_shouldThrowIfNameIsNull() {
        CustomerEmail email = new CustomerEmail("bob@example.com");
        assertThrows(IllegalArgumentException.class,
                () -> factory.createClient(null, email));
    }

    @Test
    void createClient_shouldThrowIfEmailIsNull() {
        CustomerName name = new CustomerName("Bob");
        assertThrows(IllegalArgumentException.class,
                () -> factory.createClient(name, null));
    }

    @Test
    void recreateClient_shouldReturnExactClient() {
        ClientId id = new ClientId();
        CustomerName name = new CustomerName("Charlie");
        CustomerEmail email = new CustomerEmail("charlie@example.com");
        CreationDate date = new CreationDate();

        Client client = factory.recreateClient(id, name, email, date);

        assertEquals(id, client.getId());
        assertEquals(name, client.getName());
        assertEquals(email, client.getEmail());
        assertEquals(date, client.getDate());
    }
}