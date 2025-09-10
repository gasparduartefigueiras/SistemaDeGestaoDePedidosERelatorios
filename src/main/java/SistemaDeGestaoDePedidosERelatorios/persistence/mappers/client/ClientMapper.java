package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.client;

import SistemaDeGestaoDePedidosERelatorios.VOs.ClientId;
import SistemaDeGestaoDePedidosERelatorios.VOs.CreationDate;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerEmail;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerName;
import SistemaDeGestaoDePedidosERelatorios.domain.client.Client;
import SistemaDeGestaoDePedidosERelatorios.domain.client.IClientFactory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.client.ClientDataModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Component
public class ClientMapper implements IClientMapper {

    private final IClientFactory clientFactory;

    public ClientMapper(IClientFactory clientFactory) {
        this.clientFactory = Objects.requireNonNull(clientFactory, "clientFactory");
    }

    @Override
    public ClientDataModel toDataModel(Client client) {
        Objects.requireNonNull(client, "client");

            UUID id = client.getId().getClientId();
            String name = client.getName().getCustomerName();
            String email = client.getEmail().getEmail();
            LocalDate date = client.getDate().getCreationDate();

            return new ClientDataModel(id, name, email, date);
    }


    @Override
    public Client toDomain(ClientDataModel clientDataModel) {

        try {
            Objects.requireNonNull(clientDataModel, "clientDataModel");

            ClientId id = new ClientId(clientDataModel.getId());
            CustomerName name = new CustomerName(clientDataModel.getName());
            CustomerEmail email = new CustomerEmail(clientDataModel.getEmail());
            CreationDate date = new CreationDate(clientDataModel.getDate());

            return clientFactory.recreateClient(id, name, email, date);

        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error trying to map ClientDataModel to Client", e);
        }
    }
}
