package SistemaDeGestaoDePedidosERelatorios.domain.client;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;
import org.springframework.stereotype.Component;

@Component
public class ClientFactory implements IClientFactory {

    @Override
    public Client createClient(CustomerName name, CustomerEmail email) {
        if (name == null || email == null) {
            throw new IllegalArgumentException("Name and Email cannot be null.");
        }

        ClientId id = new ClientId();
        CreationDate date = new CreationDate();

        return new Client(id, name, email, date);
    }

    @Override
    public Client recreateClient(ClientId id, CustomerName name, CustomerEmail email, CreationDate date) {
        return new Client(id, name, email, date);
    }
}
