package com.micro.notificacionesservice.Service.Strategy;

public interface NotificacionStrategy {
    void enviar(String destino, String asunto, String mensaje);
    String getNombre();
}
