package com.micro.pagosservice.Factory;

import com.micro.pagosservice.Strategy.PagoStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PagoStrategyFactory {
    private final List<PagoStrategy> estrategias;

    public PagoStrategyFactory(List<PagoStrategy> estrategias) {
        this.estrategias = estrategias;
    }

    public PagoStrategy obtenerEstrategia(String metodoPago) {
        return estrategias.stream()
                .filter(e -> e.soportaMetodo(metodoPago))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no soportado: " + metodoPago));
    }
}
