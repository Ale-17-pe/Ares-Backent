package com.micro.pagosservice.Model;

import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long usuarioId;

    @NotNull
    @Column(nullable = false)
    private Long referenciaId;

    @NotNull
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal monto;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "La moneda debe estar en formato ISO (ej: PEN, USD)")
    @Column(length = 10, nullable = false)
    private String moneda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTransaccion estado = EstadoTransaccion.PENDIENTE;

    private String metodoPago;
    private String descripcion;
    private String gatewayTxId;

    private Instant fechaCreacion;
    private Instant fechaActualizacion;

    @Column(nullable = false, unique = true, updatable = false)
    private String codigoInterno;


    @PrePersist
    public void prePersist() {
        fechaCreacion = Instant.now();
        fechaActualizacion = Instant.now();
        if (codigoInterno == null) {
            codigoInterno = "TX-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    public void preUpdate() {
        fechaActualizacion = Instant.now();
    }
}
