package SistemaDeGestaoDePedidosERelatorios.domain.client;

import SistemaDeGestaoDePedidosERelatorios.VOs.ClientId;
import SistemaDeGestaoDePedidosERelatorios.VOs.CreationDate;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerEmail;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void shouldCreateValidClient() {
        ClientId id = new ClientId();
        CustomerName name = new CustomerName("Alice");
        CustomerEmail email = new CustomerEmail("alice@example.com");
        CreationDate date = new CreationDate();

        Client client = new Client(id, name, email, date);

        assertEquals(id, client.getId());
        assertEquals(name, client.getName());
        assertEquals(email, client.getEmail());
        assertEquals(date, client.getDate());
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Client(null,
                        new CustomerName("Bob"),
                        new CustomerEmail("bob@example.com"),
                        new CreationDate()));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Client(new ClientId(),
                        null,
                        new CustomerEmail("bob@example.com"),
                        new CreationDate()));
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Client(new ClientId(),
                        new CustomerName("Bob"),
                        null,
                        new CreationDate()));
    }

    @Test
    void shouldThrowWhenDateIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Client(new ClientId(),
                        new CustomerName("Bob"),
                        new CustomerEmail("bob@example.com"),
                        null));
    }

    @Test
    void clientsWithSameIdShouldBeEqual() {
        ClientId sameId = new ClientId();

        Client client1 = new Client(sameId,
                new CustomerName("Alice"),
                new CustomerEmail("alice@example.com"),
                new CreationDate());

        Client client2 = new Client(sameId,
                new CustomerName("Bob"),
                new CustomerEmail("bob@example.com"),
                new CreationDate());

        assertEquals(client1, client2);
        assertEquals(client1.hashCode(), client2.hashCode());
    }

    @Test
    void toStringShouldContainIdAndEmail() {
        Client client = new Client(new ClientId(),
                new CustomerName("Alice"),
                new CustomerEmail("alice@example.com"),
                new CreationDate());

        String result = client.toString();
        assertTrue(result.contains("alice@example.com"));
        assertTrue(result.contains("ClientId"));
    }
}