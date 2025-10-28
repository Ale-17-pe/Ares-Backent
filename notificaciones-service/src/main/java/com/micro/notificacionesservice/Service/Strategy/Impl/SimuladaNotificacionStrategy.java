package com.micro.notificacionesservice.Service.Strategy.Impl;

import com.micro.notificacionesservice.Service.Strategy.NotificacionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimuladaNotificacionStrategy implements NotificacionStrategy {
    @Override
    public void enviar(String destino, String asunto, String mensaje) {
        log.warn("""
                🔧 [SIMULADA] Destino: {}
                Asunto: {}
                ---
                {}
                """, destino, asunto, mensaje);
    }

    @Override
    public String getNombre() {
        return "simulado notificacion strategy";
    }
}
