package com.micro.pagosservice.Strategy;

import com.micro.pagosservice.DTO.TransaccionRequest;
import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import com.micro.pagosservice.Model.Transaccion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class PaypalStrategy implements PagoStrategy {
    @Override
    public Transaccion procesarPago(TransaccionRequest request) {
        log.info("💰 Procesando pago con PayPal para usuario {}", request.getUsuarioId());

        Transaccion tx = new Transaccion();
        tx.setUsuarioId(request.getUsuarioId());
        tx.setReferenciaId(request.getReferenciaId());
        tx.setMonto(request.getMonto());
        tx.setMoneda(request.getMoneda());
        tx.setMetodoPago("PAYPAL");
        tx.setDescripcion(request.getDescripcion());

        // Simulación del proceso de autorización y confirmación de PayPal
        tx.setEstado(EstadoTransaccion.COMPLETADA);
        tx.setCodigoInterno(UUID.randomUUID().toString());
        tx.setGatewayTxId("PAYPAL-" + UUID.randomUUID());

        log.info("✅ Pago procesado exitosamente en PayPal con ID: {}", tx.getGatewayTxId());
        return tx;
    }

    @Override
    public boolean soportaMetodo(String metodo) {
        return "PAYPAL".equalsIgnoreCase(metodo);
    }
}
