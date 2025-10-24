package com.micro.notificacionesservice.Service.Impl;

import com.micro.notificacionesservice.Service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviar(String destino, String asunto, String cuerpo) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destino);
            message.setSubject(asunto);
            message.setText(cuerpo);

            System.out.println("--- ENVÍO SIMULADO EXITOSO ---");
            System.out.println("Destino: " + destino + ", Asunto: " + asunto);
            System.out.println("-----------------------------");
        }catch(Exception e){
            System.out.println("Error al enviar email: " +destino + ":"+ e.getMessage());
        }
    }
}
