package SistemaDeGestaoDePedidosERelatorios.controller;

import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderRequestDTO;
import SistemaDeGestaoDePedidosERelatorios.DTOs.order.OrderResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.service.order.IOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderRequestDTO request) {
        try {
            OrderResponseDTO dto = orderService.create(request);
            return ResponseEntity.status(201).body(dto); // 201 Created
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage()); // 400 Bad Request
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal server error"); // 500
        }
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable UUID id) {
        OrderResponseDTO dto = orderService.getOrderByID(id);
        return (dto == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    // FIND BY STATUS (ex.: /api/orders/status/APPROVED)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.findByStatus(status));
    }

    // FIND BY CREATION DATE (ex.: /api/orders/date/2025-09-01/2025-09-08)
    @GetMapping("/date/{from}/{to}")
    public ResponseEntity<List<OrderResponseDTO>> findByCreationDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(orderService.findByCreationDate(from, to));
    }
}