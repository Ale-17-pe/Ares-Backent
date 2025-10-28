package com.micro.notificacionesservice.Service;

import com.micro.notificacionesservice.Dto.MembresiaProximaAVencer;
import com.micro.notificacionesservice.Dto.PagoCompletado;

public class MensajeFactory {
    public record Mensaje(String cuerpo, String asunto) {}

    public static Mensaje paraPagoCompleto(PagoCompletado e){
        String asunto = "Comprobante de Pago Exitoso";
        String cuerpo = """
                Estimado cliente,

                Su pago de %s %s se ha procesado con éxito.
                ID de transacción: %d
                Fecha: %s

                ¡Gracias por elegir AresFitness!
                """.formatted(e.monto(), e.moneda(), e.idTransaccion(), e.fecha());
        return new Mensaje(asunto, cuerpo);
    }
    public static Mensaje paraMembresiasPorVencer(MembresiaProximaAVencer e){
        String asusto ="Recordatorio: Tu membresía por vencer";
        String cuerpo = """
                Hola Estimado cliente,
                Tu membresía esta próxima a vencer.
                No dejes que tu cambio físico pare renueva tu Membresía y sigue este gran proceso.
                
                Equipo AresFitness 💪
                 """.formatted(e.idMembresia(),e.fechaVencimiento());
        return new Mensaje(asusto, cuerpo);
    }
}
