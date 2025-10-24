package com.micro.membresiasservice.Services.Impl;

import com.micro.membresiasservice.DTO.SuscripcionRequest;
import com.micro.membresiasservice.Model.PlanMembresia;
import com.micro.membresiasservice.Model.Suscripcion;
import com.micro.membresiasservice.Repository.PlanRepository;
import com.micro.membresiasservice.Repository.SuscripcionRepository;
import com.micro.membresiasservice.Services.MembresiaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MembresiasImpl implements MembresiaService {

    private final PlanRepository planRepository;

    private final SuscripcionRepository suscripcionRepository;

    public MembresiasImpl(PlanRepository planRepository, SuscripcionRepository suscripcionRepository) {
        this.planRepository = planRepository;
        this.suscripcionRepository = suscripcionRepository;
    }

    @Override
    public PlanMembresia crearPlan(PlanMembresia planMembresia) {
        return planRepository.save(planMembresia);
    }

    @Override
    public List<PlanMembresia> listarPlanes() {
        return planRepository.findAll();
    }

    @Override
    public Optional<PlanMembresia> obtenerPlan(Long id) {
        return planRepository.findById(id);
    }

    @Override
    public PlanMembresia actualizarPlan(Long id, PlanMembresia planActualizado) {
        PlanMembresia planExistente = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan no encontrado con ID: " + id));

        planExistente.setNombrePlan(planActualizado.getNombrePlan());
        planExistente.setDescripcion(planActualizado.getDescripcion());
        planExistente.setPrecio(planActualizado.getPrecio());
        planExistente.setDuracionEnDias(planActualizado.getDuracionEnDias());

        return planRepository.save(planExistente);
    }

    @Override
    public void eliminarPlan(Long id) {
        if (!planRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar. Plan no encontrado con ID: " + id);
        }
        try {
            planRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el plan: " + e.getMessage());
        }
    }

    @Override
    public List<Suscripcion> listarTodasLasSuscripciones() {
        return suscripcionRepository.findAll();
    }

    @Override
    public Suscripcion crearSuscripcion(SuscripcionRequest request) {
        PlanMembresia plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new EntityNotFoundException("Plan no encontrado con ID: " + request.getPlanId()));

        // Opcional: Validar que el usuario no tenga ya una suscripción activa
        if (verificarSuscripcionActiva(request.getUsuarioId()).isPresent()) {
            throw new IllegalStateException("El usuario ya tiene una suscripción activa.");
        }

        Suscripcion nuevaSuscripcion = new Suscripcion();
        nuevaSuscripcion.setUsuarioId(request.getUsuarioId());
        nuevaSuscripcion.setPlan(plan);
        nuevaSuscripcion.setFechaInicio(LocalDate.now());
        nuevaSuscripcion.setFechaFin(LocalDate.now().plusDays(plan.getDuracionEnDias()));
        nuevaSuscripcion.setEstado("ACTIVA");

        return suscripcionRepository.save(nuevaSuscripcion);
    }

    @Override
    public Optional<Suscripcion> verificarSuscripcionActiva(Long usuarioId) {
        return suscripcionRepository.findByUsuarioIdAndEstado(usuarioId, "ACTIVA");
    }
}
