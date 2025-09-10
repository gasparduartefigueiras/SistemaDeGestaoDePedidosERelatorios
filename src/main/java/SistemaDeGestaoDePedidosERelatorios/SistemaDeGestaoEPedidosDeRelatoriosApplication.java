package SistemaDeGestaoDePedidosERelatorios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "SistemaDeGestaoDePedidosERelatorios.persistence.repositories")
@EntityScan(basePackages = "SistemaDeGestaoDePedidosERelatorios.persistence.dataModels")
public class SistemaDeGestaoEPedidosDeRelatoriosApplication {
    public static void main(String[] args) {
        SpringApplication.run(SistemaDeGestaoEPedidosDeRelatoriosApplication.class, args);
    }


}