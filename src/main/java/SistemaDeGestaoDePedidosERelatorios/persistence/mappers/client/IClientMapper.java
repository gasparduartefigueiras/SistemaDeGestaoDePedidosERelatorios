package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.client;

import SistemaDeGestaoDePedidosERelatorios.domain.client.Client;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.client.ClientDataModel;

public interface IClientMapper {

    ClientDataModel toDataModel(Client client);

    Client toDomain(ClientDataModel dataModel);
}