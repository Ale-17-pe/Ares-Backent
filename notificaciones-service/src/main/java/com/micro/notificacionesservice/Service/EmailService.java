package com.micro.notificacionesservice.Service;

public interface EmailService {
    void enviar(String destino, String asunto, String cuerpo);
}
