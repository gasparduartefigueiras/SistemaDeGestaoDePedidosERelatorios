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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ErrorLogServiceTest {

    private IErrorLogRepository repository;
    private IErrorLogMapper mapper;
    private IErrorLogFactory factory;
    private IEmailService emailService;

    private ErrorLogService service;

    @BeforeEach
    void setup() {
        repository = mock(IErrorLogRepository.class);
        mapper = mock(IErrorLogMapper.class);
        factory = mock(IErrorLogFactory.class);
        emailService = mock(IEmailService.class);
        service = new ErrorLogService(repository, mapper, factory, emailService);
    }

    @Test
    void errorPersistsAndSendsEmail() {
        UUID orderId = UUID.randomUUID();

        ErrorLog domainBeforeSave = mock(ErrorLog.class);
        ErrorLogDataModel dmSaved = mock(ErrorLogDataModel.class);
        ErrorLog domainAfterSave = mock(ErrorLog.class);

        when(factory.createErrorLog(any(LogLevel.class), any(LogMessage.class), any(OrderId.class)))
                .thenReturn(domainBeforeSave);
        when(mapper.toDataModel(domainBeforeSave)).thenReturn(mock(ErrorLogDataModel.class));
        when(repository.save(any(ErrorLogDataModel.class))).thenReturn(dmSaved);
        when(mapper.toDomain(dmSaved)).thenReturn(domainAfterSave);

        // Stubs necessários para não rebentar no sendIfNeeded
        when(domainAfterSave.getLevel()).thenReturn(LogLevel.error());
        when(domainAfterSave.getMessage()).thenReturn(new LogMessage("boom"));
        when(domainAfterSave.getOrderId()).thenReturn(new OrderId(orderId));

        // <-- ESTE É O PONTO QUE FALTAVA
        SistemaDeGestaoDePedidosERelatorios.VOs.LogErrorMoment moment =
                new SistemaDeGestaoDePedidosERelatorios.VOs.LogErrorMoment(java.time.LocalDateTime.now());
        when(domainAfterSave.getErrorMoment()).thenReturn(moment);

        ErrorLog result = service.error("boom", orderId);

        assertSame(domainAfterSave, result);
        verify(factory).createErrorLog(eq(LogLevel.error()), any(LogMessage.class), eq(new OrderId(orderId)));
        verify(repository).save(any(ErrorLogDataModel.class));
        verify(emailService).sendSimpleMessage(
                eq("gaspar.duarte.figueiras@gmail.com"),
                contains("[SGPR] Erro"),   // subjet contém "[SGPR] Erro na criação de Pedido"
                contains("boom")
        );
    }

    @Test
    void warnPersistsAndSendsEmail() {
        // arrange
        ErrorLog domainBeforeSave = mock(ErrorLog.class);
        ErrorLogDataModel dmUnsaved = mock(ErrorLogDataModel.class);
        ErrorLogDataModel dmSaved = mock(ErrorLogDataModel.class);
        ErrorLog domainAfterSave = mock(ErrorLog.class);

        when(factory.createErrorLog(
                any(LogLevel.class),
                any(LogMessage.class),
                (OrderId) isNull()                 // <- aqui
        )).thenReturn(domainBeforeSave);

        when(mapper.toDataModel(domainBeforeSave)).thenReturn(dmUnsaved);
        when(repository.save(dmUnsaved)).thenReturn(dmSaved);
        when(mapper.toDomain(dmSaved)).thenReturn(domainAfterSave);

        when(domainAfterSave.getLevel()).thenReturn(LogLevel.warn());
        when(domainAfterSave.getMessage()).thenReturn(new LogMessage("heads up"));
        when(domainAfterSave.getOrderId()).thenReturn(null);
        SistemaDeGestaoDePedidosERelatorios.VOs.LogErrorMoment moment =
                new SistemaDeGestaoDePedidosERelatorios.VOs.LogErrorMoment(java.time.LocalDateTime.now());
        when(domainAfterSave.getErrorMoment()).thenReturn(moment);

        // act
        ErrorLog result = service.warn("heads up", null);

        // assert
        assertSame(domainAfterSave, result);
        verify(factory).createErrorLog(any(LogLevel.class), any(LogMessage.class), (OrderId) isNull());
        verify(mapper).toDataModel(domainBeforeSave);
        verify(repository).save(dmUnsaved);
        verify(mapper).toDomain(dmSaved);
        verify(emailService).sendSimpleMessage(
                eq("gaspar.duarte.figueiras@gmail.com"),
                contains("[SGPR] Aviso"),
                contains("heads up")
        );
    }

    @Test
    void infoPersistsAndDoesNotSendEmail() {
        ErrorLog domainBeforeSave = mock(ErrorLog.class);
        ErrorLogDataModel dmSaved = mock(ErrorLogDataModel.class);
        ErrorLog domainAfterSave = mock(ErrorLog.class);

        when(factory.createErrorLog(any(LogLevel.class), any(LogMessage.class), any()))
                .thenReturn(domainBeforeSave);
        when(mapper.toDataModel(domainBeforeSave)).thenReturn(mock(ErrorLogDataModel.class));
        when(repository.save(any(ErrorLogDataModel.class))).thenReturn(dmSaved);
        when(mapper.toDomain(dmSaved)).thenReturn(domainAfterSave);

        when(domainAfterSave.getLevel()).thenReturn(LogLevel.info());

        ErrorLog result = service.info("just fyi", null);

        assertSame(domainAfterSave, result);
        verify(emailService, never()).sendSimpleMessage(anyString(), anyString(), anyString());
    }

    @Test
    void exceptionBuildsMessageAndSendsEmail() {
        ErrorLog domainBeforeSave = mock(ErrorLog.class);
        ErrorLogDataModel dmSaved = mock(ErrorLogDataModel.class);
        ErrorLog domainAfterSave = mock(ErrorLog.class);

        when(factory.createErrorLog(any(LogLevel.class), any(LogMessage.class), any()))
                .thenReturn(domainBeforeSave);
        when(mapper.toDataModel(domainBeforeSave)).thenReturn(mock(ErrorLogDataModel.class));
        when(repository.save(any(ErrorLogDataModel.class))).thenReturn(dmSaved);
        when(mapper.toDomain(dmSaved)).thenReturn(domainAfterSave);

        when(domainAfterSave.getLevel()).thenReturn(LogLevel.error());
        when(domainAfterSave.getMessage()).thenReturn(new LogMessage("oops | cause=java.lang.RuntimeException: x"));
        when(domainAfterSave.getOrderId()).thenReturn(null);
        // Também precisa de momento
        SistemaDeGestaoDePedidosERelatorios.VOs.LogErrorMoment moment =
                new SistemaDeGestaoDePedidosERelatorios.VOs.LogErrorMoment(java.time.LocalDateTime.now());
        when(domainAfterSave.getErrorMoment()).thenReturn(moment);

        service.exception("oops", null, new RuntimeException("x"));

        org.mockito.ArgumentCaptor<String> bodyCap = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).sendSimpleMessage(anyString(), anyString(), bodyCap.capture());
        String body = bodyCap.getValue();
        assertTrue(body.contains("oops"));
        assertTrue(body.contains("cause=java.lang.RuntimeException"));
    }

    @Test
    void getAllMapsDomainList() {
        ErrorLogDataModel dm1 = mock(ErrorLogDataModel.class);
        ErrorLogDataModel dm2 = mock(ErrorLogDataModel.class);
        when(repository.findAll()).thenReturn(Arrays.asList(dm1, dm2));

        ErrorLog d1 = mock(ErrorLog.class);
        ErrorLog d2 = mock(ErrorLog.class);
        when(mapper.toDomain(dm1)).thenReturn(d1);
        when(mapper.toDomain(dm2)).thenReturn(d2);

        List<ErrorLog> list = service.getAll();

        assertEquals(2, list.size());
        assertSame(d1, list.get(0));
        assertSame(d2, list.get(1));
    }

    @Test
    void getByOrderIdUsesRepositoryAndMaps() {
        UUID oid = UUID.randomUUID();

        ErrorLogDataModel dm = mock(ErrorLogDataModel.class);
        Page<ErrorLogDataModel> page = new PageImpl<ErrorLogDataModel>(Collections.singletonList(dm));
        when(repository.findByOrderId(eq(oid), any(Pageable.class))).thenReturn(page);

        ErrorLog domain = mock(ErrorLog.class);
        when(mapper.toDomain(dm)).thenReturn(domain);

        List<ErrorLog> list = service.getByOrderId(oid);

        assertEquals(1, list.size());
        assertSame(domain, list.get(0));
    }

    @Test
    void getByLevelNormalizesAndMaps() {
        ErrorLogDataModel dm = mock(ErrorLogDataModel.class);
        Page<ErrorLogDataModel> page = new PageImpl<ErrorLogDataModel>(Collections.singletonList(dm));
        when(repository.findByLogLevel(eq("ERROR"), any(Pageable.class))).thenReturn(page);

        ErrorLog domain = mock(ErrorLog.class);
        when(mapper.toDomain(dm)).thenReturn(domain);

        List<ErrorLog> list = service.getByLevel("  error  ");

        assertEquals(1, list.size());
        assertSame(domain, list.get(0));
    }
}