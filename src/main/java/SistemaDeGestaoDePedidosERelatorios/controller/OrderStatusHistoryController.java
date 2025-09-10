package SistemaDeGestaoDePedidosERelatorios.controller;

import SistemaDeGestaoDePedidosERelatorios.assembler.OrderStatusHistoryDTOAssembler;
import SistemaDeGestaoDePedidosERelatorios.DTOs.orderStatusHistory.OrderStatusHistoryResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.domain.orderStatusHistory.OrderStatusHistory;
import SistemaDeGestaoDePedidosERelatorios.service.orderStatus.IOrderStatusHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-status-history")
public class OrderStatusHistoryController {

    private final IOrderStatusHistoryService service;

    public OrderStatusHistoryController(IOrderStatusHistoryService service) {
        this.service = service;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<OrderStatusHistoryResponseDTO>> getAll() {
        List<OrderStatusHistory> list = service.findAll();
        List<OrderStatusHistoryResponseDTO> body = list.stream()
                .map(OrderStatusHistoryDTOAssembler::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    // GET BY HISTORY ID
    @GetMapping("/{historyId}")
    public ResponseEntity<OrderStatusHistoryResponseDTO> getByHistoryId(@PathVariable("historyId") UUID historyId) {
        return service.findById(historyId)
                .map(h -> ResponseEntity.ok(OrderStatusHistoryDTOAssembler.toResponseDTO(h)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET BY ORDER ID
    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<List<OrderStatusHistoryResponseDTO>> getByOrderId(@PathVariable("orderId") UUID orderId) {
        List<OrderStatusHistory> list = service.findByOrderId(orderId);
        List<OrderStatusHistoryResponseDTO> body = list.stream()
                .map(OrderStatusHistoryDTOAssembler::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }
}