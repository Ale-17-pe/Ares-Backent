package com.micro.pagosservice.DTO;

import java.math.BigDecimal;
import java.time.Instant;

public record TransaccionCompleta(Long idTransaccion,
                                  Long idUsuario,
                                  String emailDestino,
                                  BigDecimal monto,
                                  String moneda,
                                  Instant fecha) {
}
