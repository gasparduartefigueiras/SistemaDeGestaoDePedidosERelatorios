package SistemaDeGestaoDePedidosERelatorios.infrastructure;

import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerEmail;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerName;
import SistemaDeGestaoDePedidosERelatorios.VOs.ValidationOutcome;
import SistemaDeGestaoDePedidosERelatorios.config.ValidationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExternalClientValidator implements IExternalClientValidator {

    private final RestTemplate restTemplate;
    private final ValidationProperties properties;

    public ExternalClientValidator(RestTemplate restTemplate, ValidationProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public ValidationOutcome validate(CustomerName name, CustomerEmail email) {
        try {
            Map<String, String> request = new HashMap<>();
            request.put("name", name.getCustomerName());
            request.put("email", email.getEmail());

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    properties.getListUrl(),
                    request,
                    Map.class
            );

            if (response == null) {
                return new ValidationOutcome(false, "External validator returned empty response.");
            }

            Object approvedRaw = response.get("approved");
            Object validRaw = response.get("valid");

            boolean approved = false;
            if (approvedRaw instanceof Boolean && (Boolean) approvedRaw) {
                approved = true;
            } else if (validRaw instanceof Boolean && (Boolean) validRaw) {
                approved = true;
            }

            String message;
            if (response.get("message") instanceof String) {
                message = (String) response.get("message");
            } else {
                if (approved) {
                    message = "APPROVED";
                } else {
                    message = "REJECTED";
                }
            }

            return new ValidationOutcome(approved, message);

        } catch (Exception e) {
            return new ValidationOutcome(false, "Validation service unavailable.");
        }
    }
}