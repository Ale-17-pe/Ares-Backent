package com.micro.pagosservice.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransaccionRequest {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID de referencia es obligatorio")
    private Long referenciaId; // Puede ser una membresía, reserva, etc.

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "La moneda es obligatoria")
    @Pattern(regexp = "^[A-Z]{3}$", message = "La moneda debe tener formato ISO (PEN, USD, etc.)")
    private String moneda;

    @NotBlank(message = "Debe especificar el método de pago")
    private String metodoPago;

    private String descripcion;
}
