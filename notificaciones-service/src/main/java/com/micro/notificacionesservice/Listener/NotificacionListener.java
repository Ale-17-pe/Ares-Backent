// src/main/java/com/micro/notificacionesservice/Listener/NotificacionListener.java
package com.micro.notificacionesservice.Listener;

import com.micro.notificacionesservice.Dto.MembresiaProximaAVencer;
import com.micro.notificacionesservice.Dto.PagoCompletado;
import com.micro.notificacionesservice.Service.MensajeFactory;
import com.micro.notificacionesservice.Service.NotificacionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotificacionListener {

    private final NotificacionContext notificacionContext;

    @Bean
    public Consumer<PagoCompletado> procesarPago() {
        return event -> {
            log.info("💰 Evento pago recibido: {}", event);
            var msg = MensajeFactory.paraPagoCompleto(event);
            notificacionContext.enviarDefault(event.emailDestino(), msg.asunto(), msg.cuerpo());
        };
    }

    @Bean
    public Consumer<MembresiaProximaAVencer> procesarVencimiento() {
        return event -> {
            log.info("⏰ Evento vencimiento recibido: {}", event);
            var msg = MensajeFactory.paraMembresiasPorVencer(event);
            notificacionContext.enviarDefault(event.emailDestino(), msg.asunto(), msg.cuerpo());
        };
    }
}
