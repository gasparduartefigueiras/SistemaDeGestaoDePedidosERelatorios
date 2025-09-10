package SistemaDeGestaoDePedidosERelatorios.infrastructure;

import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerEmail;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerName;
import SistemaDeGestaoDePedidosERelatorios.VOs.ValidationOutcome;
import SistemaDeGestaoDePedidosERelatorios.config.ValidationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalClientValidatorTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ValidationProperties properties;

    @InjectMocks
    private ExternalClientValidator validator;

    private final String url = "http://localhost:8089/validate";

    @BeforeEach
    void setup() {
        when(properties.getListUrl()).thenReturn(url);
    }

    private CustomerName name(String v) {
        return new CustomerName(v);
    }

    private CustomerEmail email(String v) {
        return new CustomerEmail(v);
    }

    @Test
    void shouldReturnApprovedWhenResponseHasApprovedTrueAndMessage() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("approved", true);
        resp.put("message", "Approved (whitelist).");

        when(restTemplate.postForObject(eq(url), any(Map.class), eq(Map.class)))
                .thenReturn(resp);

        ValidationOutcome out = validator.validate(name("Alice"), email("alice@example.com"));

        assertTrue(out.isApproved());
        assertEquals("Approved (whitelist).", out.getMessage());
    }

    @Test
    void shouldReturnApprovedWhenValidTrueWithoutMessage() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("valid", true);

        when(restTemplate.postForObject(eq(url), any(Map.class), eq(Map.class)))
                .thenReturn(resp);

        ValidationOutcome out = validator.validate(name("Bob"), email("bob@example.com"));

        assertTrue(out.isApproved());
        assertEquals("APPROVED", out.getMessage());
    }

    @Test
    void shouldReturnRejectedWhenNoFlagsAndNoMessage() {
        Map<String, Object> resp = new HashMap<>();
        when(restTemplate.postForObject(eq(url), any(Map.class), eq(Map.class)))
                .thenReturn(resp);

        ValidationOutcome out = validator.validate(name("Charlie"), email("charlie@reject.me"));

        assertFalse(out.isApproved());
        assertEquals("REJECTED", out.getMessage());
    }

    @Test
    void shouldReturnRejectedWithCustomMessageWhenProvided() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("approved", false);
        resp.put("message", "Client rejected by WireMock.");

        when(restTemplate.postForObject(eq(url), any(Map.class), eq(Map.class)))
                .thenReturn(resp);

        ValidationOutcome out = validator.validate(name("Dave"), email("reject@test.com"));

        assertFalse(out.isApproved());
        assertEquals("Client rejected by WireMock.", out.getMessage());
    }

    @Test
    void shouldReturnUnavailableWhenResponseIsNull() {
        when(restTemplate.postForObject(eq(url), any(Map.class), eq(Map.class)))
                .thenReturn(null);

        ValidationOutcome out = validator.validate(name("Eve"), email("eve@example.com"));

        assertFalse(out.isApproved());
        assertEquals("External validator returned empty response.", out.getMessage());
    }

    @Test
    void shouldReturnUnavailableWhenExceptionOccurs() {
        when(restTemplate.postForObject(eq(url), any(Map.class), eq(Map.class)))
                .thenThrow(new RuntimeException("boom"));

        ValidationOutcome out = validator.validate(name("Frank"), email("frank@example.com"));

        assertFalse(out.isApproved());
        assertEquals("Validation service unavailable.", out.getMessage());
    }
}