package SistemaDeGestaoDePedidosERelatorios.controller;

import SistemaDeGestaoDePedidosERelatorios.DTOs.errorLog.ErrorLogResponseDTO;
import SistemaDeGestaoDePedidosERelatorios.assembler.ErrorLogDTOAssembler;
import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.ErrorLog;
import SistemaDeGestaoDePedidosERelatorios.service.errorLog.IErrorLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/api/error-logs")
public class ErrorLogController {

    private final IErrorLogService service;

    public ErrorLogController(IErrorLogService service) {
        if (service == null) throw new IllegalArgumentException("service");
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ErrorLogResponseDTO>> getAll() {
        List<ErrorLog> logs = service.getAll();
        List<ErrorLogResponseDTO> list = logs.stream()
                .map(ErrorLogDTOAssembler::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ErrorLogResponseDTO>> getByOrderId(@PathVariable("orderId") UUID orderId) {
        List<ErrorLog> logs = service.getByOrderId(orderId);
        List<ErrorLogResponseDTO> list = logs.stream()
                .map(ErrorLogDTOAssembler::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<List<ErrorLogResponseDTO>> getByLevel(@PathVariable("level") String level) {
        List<ErrorLog> logs = service.getByLevel(level);
        List<ErrorLogResponseDTO> list = logs.stream()
                .map(ErrorLogDTOAssembler::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}