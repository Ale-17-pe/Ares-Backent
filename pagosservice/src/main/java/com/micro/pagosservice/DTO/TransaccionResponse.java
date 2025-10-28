package com.micro.pagosservice.DTO;

import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import com.micro.pagosservice.Model.Transaccion;
import lombok.Data;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponse {

    private Long id;
    private String codigoInterno;

    private Long usuarioId;
    private Long referenciaId;
    private String gatewayTxId;

    private BigDecimal monto;
    private String moneda;
    private String metodoPago;

    private EstadoTransaccion estado;
    private String descripcion;

    private Instant fechaCreacion;
    private Instant fechaActualizacion;

    public static TransaccionResponse fromEntity(Transaccion tx) {
        return TransaccionResponse.builder()
                .id(tx.getId())
                .usuarioId(tx.getUsuarioId())
                .referenciaId(tx.getReferenciaId())
                .monto(tx.getMonto())
                .moneda(tx.getMoneda())
                .estado(tx.getEstado())
                .metodoPago(tx.getMetodoPago())
                .descripcion(tx.getDescripcion())
                .gatewayTxId(tx.getGatewayTxId())
                .codigoInterno(tx.getCodigoInterno())
                .fechaCreacion(tx.getFechaCreacion())
                .fechaActualizacion(tx.getFechaActualizacion())
                .build();
    }
}
