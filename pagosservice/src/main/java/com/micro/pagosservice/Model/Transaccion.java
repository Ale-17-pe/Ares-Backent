package com.micro.pagosservice.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table(name = "transacciones")
@Entity
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUsuario;

    private Long idReferencia;

    private BigDecimal monto;
    private String moneda; // Ej: "USD"

    private String estado;

    private String gatewayTxId;

    private Instant fechaCreacion = Instant.now();
    private Instant fechaActualizacion;
}