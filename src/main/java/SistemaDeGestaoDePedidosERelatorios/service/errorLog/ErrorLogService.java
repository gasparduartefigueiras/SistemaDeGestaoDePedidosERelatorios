package SistemaDeGestaoDePedidosERelatorios.service.errorLog;

import SistemaDeGestaoDePedidosERelatorios.VOs.LogLevel;
import SistemaDeGestaoDePedidosERelatorios.VOs.LogMessage;
import SistemaDeGestaoDePedidosERelatorios.VOs.OrderId;
import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.ErrorLog;
import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.IErrorLogFactory;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.errorLog.ErrorLogDataModel;
import SistemaDeGestaoDePedidosERelatorios.persistence.mappers.errorLog.IErrorLogMapper;
import SistemaDeGestaoDePedidosERelatorios.persistence.repositories.errorLog.IErrorLogRepository;
import SistemaDeGestaoDePedidosERelatorios.service.email.IEmailService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ErrorLogService implements IErrorLogService {

    private final IErrorLogRepository repository;
    private final IErrorLogMapper mapper;
    private final IErrorLogFactory factory;
    private final IEmailService emailService;

    public ErrorLogService(IErrorLogRepository repository,
                           IErrorLogMapper mapper,
                           IErrorLogFactory factory,
                           IEmailService emailService) {
        this.repository   = Objects.requireNonNull(repository);
        this.mapper       = Objects.requireNonNull(mapper);
        this.factory      = Objects.requireNonNull(factory);
        this.emailService = Objects.requireNonNull(emailService);
    }

    @Override
    public ErrorLog error(String message, UUID orderId) {
        ErrorLog log = persist(LogLevel.error(), message, orderId);
        sendIfNeeded(log);
        return log;
    }

    @Override
    public ErrorLog warn(String message, UUID orderId) {
        ErrorLog log = persist(LogLevel.warn(), message, orderId);
        sendIfNeeded(log);
        return log;
    }

    @Override
    public ErrorLog info(String message, UUID orderId) {
        return persist(LogLevel.info(), message, orderId);
    }

    @Override
    public ErrorLog exception(String message, UUID orderId, Throwable ex) {
        String base = "";
        if (message != null) {
            base = message.trim();
        }
        if (ex != null) {
            String firstLine = ex.toString();
            if (base.isEmpty()) {
                base = firstLine;
            } else {
                base = base + " | cause=" + firstLine;
            }
        }
        ErrorLog log = persist(LogLevel.error(), base, orderId);
        sendIfNeeded(log);
        return log;
    }

    private ErrorLog persist(LogLevel level, String message, UUID orderId) {
        String base;
        if (message == null || message.trim().isEmpty()) {
            base = "N/A";
        } else {
            base = message.trim();
        }

        LogMessage logMessage = new LogMessage(base);

        OrderId oid = null;
        if (orderId != null) {
            oid = new OrderId(orderId);
        }

        ErrorLog domain = factory.createErrorLog(level, logMessage, oid);
        ErrorLogDataModel dm = mapper.toDataModel(domain);
        ErrorLogDataModel saved = repository.save(dm);
        return mapper.toDomain(saved);
    }

    private void sendIfNeeded(ErrorLog log) {
        try {
            LogLevel.Level lvl = log.getLevel().getLevel();
            if (lvl == LogLevel.Level.ERROR || lvl == LogLevel.Level.WARN) {
                String subject;
                if (lvl == LogLevel.Level.ERROR) {
                    subject = "[SGPR] Erro na criação de Pedido";
                } else {
                    subject = "[SGPR] Aviso de validação de Pedido";
                }

                StringBuilder body = new StringBuilder();
                body.append("Foi registado um evento no sistema SGPR.\n\n");
                body.append("Nível: ").append(lvl).append("\n");
                body.append("Mensagem: ").append(log.getMessage().getMessage()).append("\n");

                if (log.getOrderId() != null) {
                    body.append("OrderId: ").append(log.getOrderId().getOrderId()).append("\n");
                }

                body.append("Quando: ").append(log.getErrorMoment().getDateTime()).append("\n\n");
                body.append("Por favor verifique o sistema para mais detalhes.");

                emailService.sendSimpleMessage(
                        "gaspar.duarte.figueiras@gmail.com",
                        subject,
                        body.toString()
                );
            }
        } catch (Exception ignore) {
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ErrorLog> getAll() {
        List<ErrorLogDataModel> all = repository.findAll();
        return all.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ErrorLog> getByOrderId(UUID orderId) {
        List<ErrorLogDataModel> page = repository
                .findByOrderId(orderId, Pageable.unpaged())
                .getContent();
        return page.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ErrorLog> getByLevel(String level) {
        String normalized = "";
        if (level != null) {
            normalized = level.trim().toUpperCase(java.util.Locale.ENGLISH);
        }
        List<ErrorLogDataModel> page = repository
                .findByLogLevel(normalized, Pageable.unpaged())
                .getContent();
        return page.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}