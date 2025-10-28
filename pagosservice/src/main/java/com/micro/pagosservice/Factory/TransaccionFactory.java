package com.micro.pagosservice.Factory;

import com.micro.pagosservice.DTO.TransaccionRequest;
import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import com.micro.pagosservice.Model.Transaccion;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TransaccionFactory {

    private TransaccionFactory() {
    }

    public static Transaccion crearDesdeRequest(TransaccionRequest request) {
        Transaccion transaccion = new Transaccion();
        transaccion.setUsuarioId(request.getUsuarioId());
        transaccion.setReferenciaId(request.getReferenciaId());
        transaccion.setMonto(request.getMonto());
        transaccion.setMoneda(request.getMoneda());
        transaccion.setMetodoPago(request.getMetodoPago());
        transaccion.setDescripcion(request.getDescripcion());
        transaccion.setEstado(EstadoTransaccion.PENDIENTE);
        transaccion.setCodigoInterno("TX-" + System.currentTimeMillis());
        transaccion.setFechaCreacion(Instant.now());
        transaccion.setFechaActualizacion(Instant.now());
        return transaccion;
    }
}
