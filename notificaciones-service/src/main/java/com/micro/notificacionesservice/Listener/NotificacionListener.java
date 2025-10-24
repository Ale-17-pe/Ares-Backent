package com.micro.notificacionesservice.Listener;

import com.micro.notificacionesservice.Dto.MembresiaProximaAVencer;
import com.micro.notificacionesservice.Dto.PagoCompletado;
import com.micro.notificacionesservice.Service.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class NotificacionListener {

    private final EmailService emailService;

    public NotificacionListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Bean
    public Consumer<PagoCompletado> procesarPago() {
        return event -> {
            System.out.println("\n✅ EVENTO PAGO RECIBIDO: ID Transacción: " + event.idTransaccion());

            String asunto = "Comprobante de Pago Exitoso";
            String cuerpo = String.format("Estimado cliente, su pago de %s %s ha sido procesado con éxito. ID: %d",
                    event.monto(), event.moneda(), event.idTransaccion());
            emailService.enviar(event.emailDestino(), asunto, cuerpo);
        };
    }

    @Bean
    public Consumer<MembresiaProximaAVencer> procesarVencimiento() {
        return event -> {
            System.out.println("\n EVENTO VENCIMIENTO RECIBIDO: Membresía ID: " + event.idMembresia());

            String asunto = "¡Recordatorio! Tu Membresía Vence Pronto";
            String cuerpo = String.format("Hola, te recordamos que tu membresía (%d) vence el %s. ¡Actualízala!",
                    event.idMembresia(), event.fechaVencimiento());

            emailService.enviar(event.emailDestino(), asunto, cuerpo);
        };
    }
}
