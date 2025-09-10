package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.client;

import SistemaDeGestaoDePedidosERelatorios.VOs.ClientId;
import SistemaDeGestaoDePedidosERelatorios.VOs.CreationDate;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerEmail;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerName;
import SistemaDeGestaoDePedidosERelatorios.domain.client.Client;
import SistemaDeGestaoDePedidosERelatorios.domain.client.IClientFactory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.client.ClientDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientMapperTest {

    private IClientFactory clientFactory;
    private ClientMapper mapper;

    @BeforeEach
    void setup() {
        clientFactory = mock(IClientFactory.class);
        mapper = new ClientMapper(clientFactory);
    }

    @Test
    void constructorRequiresFactory() {
        assertThrows(NullPointerException.class, () -> new ClientMapper(null));
    }

    @Test
    void toDataModelMapsAllFieldsFromValueObjects() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Alice";
        String email = "alice@example.com";
        LocalDate date = LocalDate.of(2025, 1, 15);

        Client client = mock(Client.class);
        ClientId clientId = mock(ClientId.class);
        CustomerName customerName = mock(CustomerName.class);
        CustomerEmail customerEmail = mock(CustomerEmail.class);
        CreationDate creationDate = mock(CreationDate.class);

        when(client.getId()).thenReturn(clientId);
        when(client.getName()).thenReturn(customerName);
        when(client.getEmail()).thenReturn(customerEmail);
        when(client.getDate()).thenReturn(creationDate);

        when(clientId.getClientId()).thenReturn(id);
        when(customerName.getCustomerName()).thenReturn(name);
        when(customerEmail.getEmail()).thenReturn(email);
        when(creationDate.getCreationDate()).thenReturn(date);

        // Act
        ClientDataModel dm = mapper.toDataModel(client);

        // Assert
        assertNotNull(dm);
        assertEquals(id, dm.getId());
        assertEquals(name, dm.getName());
        assertEquals(email, dm.getEmail());
        assertEquals(date, dm.getDate());

        verify(client, times(1)).getId();
        verify(client, times(1)).getName();
        verify(client, times(1)).getEmail();
        verify(client, times(1)).getDate();

        verify(clientId, times(1)).getClientId();
        verify(customerName, times(1)).getCustomerName();
        verify(customerEmail, times(1)).getEmail();
        verify(creationDate, times(1)).getCreationDate();

        verifyNoMoreInteractions(client, clientId, customerName, customerEmail, creationDate);
        verifyNoInteractions(clientFactory);
    }

    @Test
    void toDataModelThrowsOnNullClient() {
        assertThrows(NullPointerException.class, () -> mapper.toDataModel(null));
        verifyNoInteractions(clientFactory);
    }

    @Test
    void toDomainUsesFactoryAndReturnsDomainClient() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Bob";
        String email = "bob@example.com";
        LocalDate date = LocalDate.of(2024, 12, 31);

        ClientDataModel dm = new ClientDataModel(id, name, email, date);

        ClientId clientId = new ClientId(id);
        CustomerName customerName = new CustomerName(name);
        CustomerEmail customerEmail = new CustomerEmail(email);
        CreationDate creationDate = new CreationDate(date);

        Client domainClient = mock(Client.class);

        when(clientFactory.recreateClient(
                any(ClientId.class),
                any(CustomerName.class),
                any(CustomerEmail.class),
                any(CreationDate.class)
        )).thenReturn(domainClient);

        // Act
        Client result = mapper.toDomain(dm);

        // Assert
        assertNotNull(result);
        assertSame(domainClient, result);

        // Capturar por argumentos seria overkill aqui; verificamos invocação e tipos
        verify(clientFactory, times(1)).recreateClient(
                any(ClientId.class), any(CustomerName.class), any(CustomerEmail.class), any(CreationDate.class)
        );
        verifyNoMoreInteractions(clientFactory);
    }

    @Test
    void toDomainWrapsRuntimeExceptionsFromFactory() {
        // Arrange
        ClientDataModel dm = new ClientDataModel(UUID.randomUUID(), "X", "x@x.com", LocalDate.now());

        when(clientFactory.recreateClient(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("dados inválidos"));

        // Act + Assert
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(dm));
        assertTrue(ex.getMessage().contains("ClientDataModel to Client")
                        || ex.getMessage().contains("ClientDataModel para Client")
                        || ex.getMessage().contains("mapear ClientDataModel"),
                "Mensagem deve indicar mapeamento ClientDataModel -> Client");
        assertTrue(ex.getCause() instanceof IllegalArgumentException);

        verify(clientFactory, times(1)).recreateClient(any(), any(), any(), any());
        verifyNoMoreInteractions(clientFactory);
    }
}