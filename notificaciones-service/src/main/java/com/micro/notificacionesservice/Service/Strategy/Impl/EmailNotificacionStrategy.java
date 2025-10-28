package com.micro.notificacionesservice.Service.Strategy.Impl;

import com.micro.notificacionesservice.Service.Strategy.NotificacionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificacionStrategy implements NotificacionStrategy {
    private final JavaMailSender mailSender;

    public EmailNotificacionStrategy(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviar(String destino, String asunto, String mensaje) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(destino);
            mail.setSubject(asunto);
            mail.setText(mensaje);
            mailSender.send(mail);
            log.info("✅ [EMAIL] Enviado a {} | {}", destino, asunto);
        } catch (Exception e) {
            log.error("❌ [EMAIL] Fallo enviando a {}: {}", destino, e.getMessage());
            throw e;
        }

    }

    @Override
    public String getNombre() {
        return "email";
    }
}
