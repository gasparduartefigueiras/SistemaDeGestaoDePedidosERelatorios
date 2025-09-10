package SistemaDeGestaoDePedidosERelatorios.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "validation")
public class ValidationProperties {
    private String listUrl;
    private String clientId;
    private String clientSecret;

    public String getListUrl() {
        return listUrl;
    }
    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getClientSecret() {
        return clientSecret;
    }
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}