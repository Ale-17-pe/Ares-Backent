package com.micro.notificacionesservice.Dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PagoCompletado(
        Long idTransaccion,
        Long idUsuario,
        String emailDestino,
        BigDecimal monto,
        String moneda,
        Instant fecha
) {}