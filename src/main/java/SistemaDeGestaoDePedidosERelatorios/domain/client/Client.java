package SistemaDeGestaoDePedidosERelatorios.domain.client;

import SistemaDeGestaoDePedidosERelatorios.VOs.ClientId;
import SistemaDeGestaoDePedidosERelatorios.VOs.CreationDate;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerEmail;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerName;

public class Client {

    private final ClientId id;
    private final CustomerName name;
    private final CustomerEmail email;
    private final CreationDate date;

    public Client(ClientId id, CustomerName name, CustomerEmail email, CreationDate date) {

        if (id == null) {
            throw new IllegalArgumentException("ClientId cannot be null.");
        }
        if (name == null) {
            throw new IllegalArgumentException("CustomerName cannot be null.");
        }
        if (email == null) {
            throw new IllegalArgumentException("CustomerEmail cannot be null.");
        }

        if (date == null) {
            throw new IllegalArgumentException("CreationDate cannot be null.");
        }

        this.id = id;
        this.name = name;
        this.email = email;
        this.date = date;
    }

    public ClientId getId() {
        return id;
    }

    public CustomerName getName() {
        return name;
    }

    public CustomerEmail getEmail() {
        return email;
    }

    public CreationDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Client{id=" + id + ", email=" + email + "}";
    }
}