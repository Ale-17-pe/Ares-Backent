package com.micro.membresiasservice.Repository;

import com.micro.membresiasservice.Model.Enum.EstadoSuscripcion;
import com.micro.membresiasservice.Model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    // Buscar suscripción activa de un usuario
    Optional<Suscripcion> findByUsuarioIdAndEstado(Long usuarioId, EstadoSuscripcion estado);

    // Listar suscripciones activas (útil para vencimientos automáticos)
    List<Suscripcion> findByEstado(EstadoSuscripcion estado);

    // Buscar suscripciones que vencen antes de cierta fecha (para notificaciones)
    @Query("SELECT s FROM Suscripcion s WHERE s.fechaFin <= :fecha AND s.estado = 'ACTIVA'")
    List<Suscripcion> findSuscripcionesPorVencer(LocalDate fecha);

    // Buscar todas las suscripciones de un usuario
    List<Suscripcion> findByUsuarioId(Long usuarioId);
}
