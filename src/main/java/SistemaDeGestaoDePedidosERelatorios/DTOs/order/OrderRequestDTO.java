package SistemaDeGestaoDePedidosERelatorios.DTOs.order;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class OrderRequestDTO {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Customer email must be valid")
    private String customerEmail;

    @Positive(message = "Order value must be greater than zero")
    private double orderValue;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(String customerName, String customerEmail, double orderValue) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderValue = orderValue;
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

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }
}