package SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.errorLog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ErrorLog")
public class ErrorLogDataModel {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "orderId")
    private UUID orderId;

    @Column(name = "logLevel", nullable = false)
    private String logLevel;

    @Column(name = "logMessage", nullable = false)
    private String logMessage;

    @Column(name = "logErrorMoment", nullable = false)
    private LocalDateTime logErrorMoment;

    protected ErrorLogDataModel() {
    }

    public ErrorLogDataModel(UUID id, UUID orderId, String level, String message, LocalDateTime loggedAt) {
        this.id = id;
        this.orderId = orderId;
        this.logLevel = level;
        this.logMessage = message;
        this.logErrorMoment = loggedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public LocalDateTime getLogErrorMoment() {
        return logErrorMoment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorLogDataModel)) return false;
        ErrorLogDataModel that = (ErrorLogDataModel) o;
        if (this.id == null || that.id == null) return false;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "ErrorLogDataModel{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", logLevel=" + logLevel +
                ", logErrorMoment=" + logErrorMoment +
                '}';
    }
}