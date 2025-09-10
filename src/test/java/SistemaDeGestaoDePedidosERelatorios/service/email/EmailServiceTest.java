package SistemaDeGestaoDePedidosERelatorios.service.email;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Test
    void sendSimpleMessageDelegatesToJavaMailSender() {
        JavaMailSender sender = mock(JavaMailSender.class);
        EmailService service = new EmailService(sender);

        service.sendSimpleMessage("to@x.com", "sub", "body");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(sender).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();
        assertEquals("gaspar.duarte.figueiras@gmail.com", msg.getFrom());
        assertEquals("to@x.com", msg.getTo()[0]);
        assertEquals("sub", msg.getSubject());
        assertEquals("body", msg.getText());
    }
}