package SistemaDeGestaoDePedidosERelatorios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(auth -> auth
                        .antMatchers("/api/orders/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt()); // Spring resolve o decoder via issuer-uri

        return http.build();
    }
}