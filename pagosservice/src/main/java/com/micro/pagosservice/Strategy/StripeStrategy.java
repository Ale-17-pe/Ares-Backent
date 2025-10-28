package com.micro.pagosservice.Strategy;

import com.micro.pagosservice.DTO.TransaccionRequest;
import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import com.micro.pagosservice.Model.Transaccion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class StripeStrategy implements PagoStrategy {
    @Override
    public Transaccion procesarPago(TransaccionRequest request) {
        log.info("💳 Procesando pago con Stripe para usuario {}", request.getUsuarioId());

        Transaccion tx = new Transaccion();
        tx.setUsuarioId(request.getUsuarioId());
        tx.setReferenciaId(request.getReferenciaId());
        tx.setMonto(request.getMonto());
        tx.setMoneda(request.getMoneda());
        tx.setMetodoPago("STRIPE");
        tx.setDescripcion(request.getDescripcion());
        tx.setEstado(EstadoTransaccion.COMPLETADA);
        tx.setCodigoInterno(UUID.randomUUID().toString());
        tx.setGatewayTxId("STRIPE-" + UUID.randomUUID());

        return tx;
    }

    @Override
    public boolean soportaMetodo(String metodo) {
        return "STRIPE".equalsIgnoreCase(metodo);
    }
}

