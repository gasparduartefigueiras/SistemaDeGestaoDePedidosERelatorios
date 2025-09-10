package SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.client;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "Client")
public class ClientDataModel {

    @Id
    @Column (name = "id", nullable = false)
    private UUID id;

    @Column (name = "customerName", nullable = false)
    private String name;

    @Column (name = "customerEmail", nullable = false)
    private String email;

    @Column (name = "creationDate", nullable = false)
    private LocalDate date;

    protected ClientDataModel() {
    }

    public ClientDataModel(UUID id, String name, String email, LocalDate date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientDataModel)) return false;
        ClientDataModel that = (ClientDataModel) o;
        if (this.id == null || that.id == null) return false;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ClientDataModel{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}