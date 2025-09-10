package SistemaDeGestaoDePedidosERelatorios.domain.client;

import SistemaDeGestaoDePedidosERelatorios.VOs.ClientId;
import SistemaDeGestaoDePedidosERelatorios.VOs.CreationDate;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerEmail;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerName;

public interface IClientFactory {

    Client createClient(CustomerName name, CustomerEmail email);

    Client recreateClient(ClientId id, CustomerName name, CustomerEmail email, CreationDate date);

}
