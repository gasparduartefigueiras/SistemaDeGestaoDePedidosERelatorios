package SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "Orders")

public class OrderDataModel {

    @Id
    @Column (name = "id", nullable = false)
    private UUID id;

    @Column (name = "customerName", nullable = false)
    private String customerName;

    @Column (name = "customerEmail", nullable = false)
    private String customerEmail;

    @Column (name = "orderValue", nullable = false)
    private double orderValue;

    @Column (name = "creationDate", nullable = false)
    private LocalDate creationDate;

    @Column (name = "status", nullable = false)
    private String status;

    @Column (name = "validationMessage")
    private String validationMessage;

    protected OrderDataModel() {
    }

    public OrderDataModel(UUID id,
                          String customerName,
                          String customerEmail,
                          Double orderValue,
                          LocalDate creationDate,
                          String status,
                          String validationMessage) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderValue = orderValue;
        this.creationDate = creationDate;
        this.status = status;
        this.validationMessage = validationMessage;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Double getOrderValue() {
        return orderValue;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getStatus() {
        return status;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDataModel)) return false;
        OrderDataModel that = (OrderDataModel) o;
        if (this.id == null || that.id == null) return false;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderDataModel{" +
                "id=" + id +
                ", customerEmail='" + customerEmail + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}