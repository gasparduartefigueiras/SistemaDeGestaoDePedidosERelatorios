package SistemaDeGestaoDePedidosERelatorios.DTOs.errorLog;

import java.time.LocalDateTime;
import java.util.UUID;

public class ErrorLogResponseDTO {
    private UUID id;
    private UUID orderId;
    private String level;
    private String message;
    private LocalDateTime timestamp;

    public ErrorLogResponseDTO(UUID id, UUID orderId, String level, String message, LocalDateTime timestamp) {
        this.id = id;
        this.orderId = orderId;
        this.level = level;
        this.message = message;
        this.timestamp = timestamp;
    }

    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public String getLevel() { return level; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}