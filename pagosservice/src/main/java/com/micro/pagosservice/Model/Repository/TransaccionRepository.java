package com.micro.pagosservice.Model.Repository;

import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import com.micro.pagosservice.Model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    // Buscar transacciones por usuario
    List<Transaccion> findByUsuarioId(Long usuarioId);

    // Buscar por usuario y estado
    List<Transaccion> findByUsuarioIdAndEstado(Long usuarioId, EstadoTransaccion estado);

    // Buscar por ID de referencia (suscripción, pedido, etc.)
    List<Transaccion> findByReferenciaId(Long referenciaId);

    // Buscar transacción por ID de gateway
    Optional<Transaccion> findByGatewayTxId(String gatewayTxId);

    // Listar todas las transacciones completadas recientemente
    @Query("SELECT t FROM Transaccion t WHERE t.estado = 'COMPLETADA' ORDER BY t.fechaCreacion DESC")
    List<Transaccion> findUltimasCompletadas();

}
