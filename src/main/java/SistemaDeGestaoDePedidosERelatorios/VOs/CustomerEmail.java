package SistemaDeGestaoDePedidosERelatorios.VOs;

import java.util.Objects;
import java.util.regex.Pattern;

public class CustomerEmail {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private final String customerEmail;

    public CustomerEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }

        String correctEmail = email.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(correctEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }

        this.customerEmail = correctEmail;
    }

    public String getEmail() {
        return customerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEmail that = (CustomerEmail) o;
        return Objects.equals(customerEmail, that.customerEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerEmail);
    }

    @Override
    public String toString() {
        return "CustomerEmail{" + customerEmail + "}";
    }
}