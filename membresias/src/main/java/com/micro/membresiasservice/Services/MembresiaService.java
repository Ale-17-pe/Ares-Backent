package com.micro.membresiasservice.Services;

import com.micro.membresiasservice.DTO.SuscripcionRequest;
import com.micro.membresiasservice.Model.PlanMembresia;
import com.micro.membresiasservice.Model.Suscripcion;

import java.util.List;
import java.util.Optional;

public interface MembresiaService {
    PlanMembresia crearPlan(PlanMembresia planMembresia);
    List<PlanMembresia> listarPlanes();
    Optional<PlanMembresia> obtenerPlan(Long id);
    PlanMembresia actualizarPlan(Long id, PlanMembresia planActualizado);
    void eliminarPlan(Long id);

    List<Suscripcion> listarTodasLasSuscripciones(); 
    Suscripcion crearSuscripcion(SuscripcionRequest request);
    Optional<Suscripcion> verificarSuscripcionActiva(Long usuarioId);
}

