package SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.orderStatusHistory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "OrderStatusHistory")

public class OrderStatusHistoryDataModel {

    @Id
    @Column (name  = "id", nullable = false)
    private UUID id;

    @Column (name = "orderId", nullable = false)
    private UUID orderId;

    @Column (name = "initialStatus", nullable = false)
    private String initialStatus;

    @Column (name = "finalStatus", nullable = false)
    private String finalStatus;

    @Column(name = "reason")
    private String reason;

    @Column(name = "changedBy", nullable = false)
    private String changedBy;

    @Column (name = "changedAt", nullable = false)
    private LocalDateTime changedAt;

    protected OrderStatusHistoryDataModel() {

    }

    public OrderStatusHistoryDataModel(UUID id,
                                       UUID orderId,
                                       String initialStatus,
                                       String finalStatus,
                                       String reason,
                                       String changedBy,
                                       LocalDateTime changedAt) {
        this.id = id;
        this.orderId = orderId;
        this.initialStatus = initialStatus;
        this.finalStatus = finalStatus;
        this.reason = reason;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getInitialStatus() {
        return initialStatus;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public String getReason() {
        return reason;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderStatusHistoryDataModel)) return false;
        OrderStatusHistoryDataModel that = (OrderStatusHistoryDataModel) o;
        if (this.id == null || that.id == null) return false;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderStatusHistoryDataModel{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", finalStatus='" + finalStatus + '\'' +
                ", changedAt=" + changedAt +
                '}';
    }
}
