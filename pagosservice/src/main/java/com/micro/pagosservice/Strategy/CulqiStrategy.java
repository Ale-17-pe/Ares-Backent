package com.micro.pagosservice.Strategy;

import com.micro.pagosservice.DTO.TransaccionRequest;
import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import com.micro.pagosservice.Model.Transaccion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CulqiStrategy implements  PagoStrategy {
    @Override
    public Transaccion procesarPago(TransaccionRequest request) {
        log.info("💳 Procesando pago con Culqi para usuario {}", request.getUsuarioId());

        Transaccion tx = new Transaccion();
        tx.setUsuarioId(request.getUsuarioId());
        tx.setReferenciaId(request.getReferenciaId());
        tx.setMonto(request.getMonto());
        tx.setMoneda(request.getMoneda());
        tx.setMetodoPago("CULQI");
        tx.setDescripcion(request.getDescripcion());
        tx.setEstado(EstadoTransaccion.COMPLETADA);
        tx.setCodigoInterno(UUID.randomUUID().toString());

        // Simulación de respuesta del gateway
        tx.setGatewayTxId("CULQI-" + UUID.randomUUID());
        return tx;
    }

    @Override
    public boolean soportaMetodo(String metodo) {
        return "CULQI".equalsIgnoreCase(metodo);
    }
}
