package SistemaDeGestaoDePedidosERelatorios.persistence.repositories.client;

import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.client.ClientDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IClientRepository extends JpaRepository<ClientDataModel, UUID> {
    Optional<ClientDataModel> findByEmail(String email);
    Page<ClientDataModel> findByNameContainingIgnoreCase(String name, Pageable pageable);
}