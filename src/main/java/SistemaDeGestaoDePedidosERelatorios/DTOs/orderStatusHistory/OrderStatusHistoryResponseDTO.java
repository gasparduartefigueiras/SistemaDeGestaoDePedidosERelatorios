package SistemaDeGestaoDePedidosERelatorios.DTOs.orderStatusHistory;

import java.time.Instant;

public final class OrderStatusHistoryResponseDTO {

    private final String id;
    private final String orderId;
    private final String initialStatus;
    private final String finalStatus;
    private final String reason;
    private final String changedBy;
    private final Instant changedAt;

    public OrderStatusHistoryResponseDTO(String id,
                                 String orderId,
                                 String initialStatus,
                                 String finalStatus,
                                 String reason,
                                 String changedBy,
                                 Instant changedAt) {
        this.id = id;
        this.orderId = orderId;
        this.initialStatus = initialStatus;
        this.finalStatus = finalStatus;
        this.reason = reason;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getInitialStatus() { return initialStatus; }
    public String getFinalStatus() { return finalStatus; }
    public String getReason() { return reason; }
    public String getChangedBy() { return changedBy; }
    public Instant getChangedAt() { return changedAt; }
}