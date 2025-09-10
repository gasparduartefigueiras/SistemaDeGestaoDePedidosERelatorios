package SistemaDeGestaoDePedidosERelatorios.DTOs.order;

public class OrderResponseDTO {

    private String id;
    private String customerName;
    private String customerEmail;
    private double orderValue;
    private String status;
    private String createdAt;
    private String validationMessage;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(String id,
                            String customerName,
                            String customerEmail,
                            double orderValue,
                            String status,
                            String createdAt,
                            String validationMessage) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderValue = orderValue;
        this.status = status;
        this.createdAt = createdAt;
        this.validationMessage = validationMessage;
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
}