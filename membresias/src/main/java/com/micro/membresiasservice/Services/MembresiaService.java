package com.micro.membresiasservice.Services;

import com.micro.membresiasservice.DTO.SuscripcionRequest;
import com.micro.membresiasservice.Model.PlanMembresia;
import com.micro.membresiasservice.Model.Suscripcion;
import com.micro.membresiasservice.Model.Enum.EstadoSuscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MembresiaService {

    // 🔹 Gestión de Planes
    PlanMembresia crearPlan(PlanMembresia planMembresia);
    List<PlanMembresia> listarPlanes();
    Optional<PlanMembresia> obtenerPlan(Long id);
    PlanMembresia actualizarPlan(Long id, PlanMembresia planActualizado);
    void eliminarPlan(Long id);

    // 🔹 Gestión de Suscripciones
    List<Suscripcion> listarTodasLasSuscripciones();
    Page<Suscripcion> listarSuscripcionesPaginadas(Pageable pageable);
    Optional<Suscripcion> obtenerSuscripcion(Long id);
    Suscripcion crearSuscripcion(SuscripcionRequest request);
    Optional<Suscripcion> verificarSuscripcionActiva(Long usuarioId);
    Suscripcion cancelarSuscripcion(Long id);
    Suscripcion actualizarEstado(Long id, EstadoSuscripcion nuevoEstado);
    List<Suscripcion> listarPorUsuario(Long usuarioId);
}
