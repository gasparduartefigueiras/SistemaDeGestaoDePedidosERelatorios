package SistemaDeGestaoDePedidosERelatorios.domain.order;

import SistemaDeGestaoDePedidosERelatorios.VOs.*;

public class Order {

    private final OrderId id;
    private final CustomerName customerName;
    private final CustomerEmail customerEmail;
    private final OrderValue orderValue;
    private final CreationDate creationDate;
    private OrderStatus status;
    private ValidationMessage validationMessage;

    public Order(OrderId id, CustomerName customerName, CustomerEmail customerEmail,
                 OrderValue orderValue, CreationDate creationDate, OrderStatus status,
                 ValidationMessage validationMessage) {

        if (id == null) {
            throw new IllegalArgumentException("OrderId cannot be null.");
        }
        if (customerName == null) {
            throw new IllegalArgumentException("CustomerName cannot be null.");
        }
        if (customerEmail == null) {
            throw new IllegalArgumentException("CustomerEmail cannot be null.");
        }
        if (orderValue == null) {
            throw new IllegalArgumentException("OrderValue cannot be null.");
        }
        if (creationDate == null) {
            throw new IllegalArgumentException("CreationDate cannot be null.");
        }
        if (status == null) {
            throw new IllegalArgumentException("OrderStatus cannot be null.");
        }

        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderValue = orderValue;
        this.creationDate = creationDate;
        this.status = status;
        this.validationMessage = validationMessage;
    }

    public OrderId getId() {
        return id;
    }

    public CustomerName getCustomerName() {
        return customerName;
    }

    public CustomerEmail getCustomerEmail() {
        return customerEmail;
    }

    public OrderValue getOrderValue() {
        return orderValue;
    }

    public CreationDate getCreationDate() {
        return creationDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ValidationMessage getValidationMessage() {
        return validationMessage;
    }

    public void approve() {
        if (status.getOrderStatus() != OrderStatus.Status.PENDING) {
            throw new IllegalStateException("Only pending orders can be approved.");
        }
        this.status = OrderStatus.approved();
    }

    public void reject() {
        if (status.getOrderStatus() != OrderStatus.Status.PENDING) {
            throw new IllegalStateException("Only pending orders can be rejected.");
        }
        this.status = OrderStatus.rejected();
    }

    public void setValidationResult(ValidationMessage validationMessage) {
        this.validationMessage = validationMessage;
    }

    public boolean isValidated() {
        return validationMessage != null;
    }

    public void applyValidationResult(ValidationOutcome outcome) {
        if (outcome == null) {
            throw new IllegalArgumentException("Validation outcome cannot be null.");
        }
        if (this.status.getOrderStatus() != OrderStatus.Status.PENDING) {
            throw new IllegalStateException("Validation can only be applied to PENDING orders.");
        }
        if (this.validationMessage != null) {
            throw new IllegalStateException("Order already has a validation result.");
        }

        if (outcome.isApproved()) {
            this.validationMessage = ValidationMessage.success(outcome.getMessage());
            this.approve();
        } else {
            this.validationMessage = ValidationMessage.error(outcome.getMessage());
            this.reject();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", status=" + status + "}";
    }
}