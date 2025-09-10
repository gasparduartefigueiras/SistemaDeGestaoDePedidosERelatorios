package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;
import java.util.UUID;

public class ClientId {

    private final UUID clientId;

    public ClientId() {
        this.clientId = UUID.randomUUID();
    }

    public ClientId(UUID clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("ClientId cannot be null.");
        }
        this.clientId = clientId;
    }

    public UUID getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientId that = (ClientId) o;
        return Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }

    @Override
    public String toString() {
        return "ClientId{" + clientId + "}";
    }
}