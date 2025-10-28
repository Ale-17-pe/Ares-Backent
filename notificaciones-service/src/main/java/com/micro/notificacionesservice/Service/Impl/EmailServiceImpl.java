package com.micro.notificacionesservice.Service.Impl;

import com.micro.notificacionesservice.Service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviar(String destino, String asunto, String cuerpo) {
        try {
            // Envío real
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destino);
            message.setSubject(asunto);
            message.setText(cuerpo);
            mailSender.send(message);

            log.info("✅ Correo enviado a {} con asunto '{}'", destino, asunto);
        } catch (Exception e) {
            log.warn("⚠️ Envío simulado: {}, asunto: {}. Error real: {}", destino, asunto, e.getMessage());
            System.out.println("--- ENVÍO SIMULADO ---\n" +
                    "Destino: " + destino + "\nAsunto: " + asunto + "\nCuerpo: " + cuerpo);
        }
    }
}
