package com.micro.membresiasservice.Services.Impl;

import com.micro.membresiasservice.DTO.SuscripcionRequest;
import com.micro.membresiasservice.Model.Enum.EstadoSuscripcion;
import com.micro.membresiasservice.Model.PlanMembresia;
import com.micro.membresiasservice.Model.Suscripcion;
import com.micro.membresiasservice.Repository.PlanRepository;
import com.micro.membresiasservice.Repository.SuscripcionRepository;
import com.micro.membresiasservice.Services.MembresiaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MembresiasServiceImpl implements MembresiaService {

    private final PlanRepository planRepository;

    private final SuscripcionRepository suscripcionRepository;

    public MembresiasServiceImpl(PlanRepository planRepository, SuscripcionRepository suscripcionRepository) {
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
    public Page<Suscripcion> listarSuscripcionesPaginadas(Pageable pageable) {
        return suscripcionRepository.findAll(pageable);
    }

    @Override
    public Optional<Suscripcion> obtenerSuscripcion(Long id) {
        return suscripcionRepository.findById(id);
    }

    @Override
    public Suscripcion crearSuscripcion(SuscripcionRequest request) {
        PlanMembresia plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new EntityNotFoundException("Plan no encontrado con ID: " + request.getPlanId()));

        // Validar suscripción activa
        if (verificarSuscripcionActiva(request.getUsuarioId()).isPresent()) {
            throw new IllegalStateException("El usuario ya tiene una suscripción activa.");
        }

        // Crear nueva suscripción
        Suscripcion nueva = new Suscripcion();
        nueva.setUsuarioId(request.getUsuarioId());
        nueva.setPlan(plan);
        nueva.setFechaInicio(LocalDate.now());
        nueva.setFechaFin(LocalDate.now().plusDays(plan.getDuracionEnDias()));
        nueva.setEstado(EstadoSuscripcion.ACTIVA);

        log.info("Suscripción creada para usuario {} con plan {}", request.getUsuarioId(), plan.getNombrePlan());
        return suscripcionRepository.save(nueva);
    }

    @Override
    public Optional<Suscripcion> verificarSuscripcionActiva(Long usuarioId) {
        Optional<Suscripcion> suscripcionOpt = suscripcionRepository.findByUsuarioIdAndEstado(usuarioId, EstadoSuscripcion.ACTIVA);

        suscripcionOpt.ifPresent(s -> {
            if (s.getFechaFin().isBefore(LocalDate.now())) {
                s.setEstado(EstadoSuscripcion.VENCIDA);
                suscripcionRepository.save(s);
                log.info("Suscripción {} marcada como VENCIDA automáticamente.", s.getId());
            }
        });

        return suscripcionOpt.filter(s -> s.getEstado() == EstadoSuscripcion.ACTIVA);
    }

    @Override
    public Suscripcion cancelarSuscripcion(Long id) {
        Suscripcion suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada con ID: " + id));

        if (suscripcion.getEstado() == EstadoSuscripcion.CANCELADA) {
            throw new IllegalStateException("La suscripción ya está cancelada.");
        }

        suscripcion.setEstado(EstadoSuscripcion.CANCELADA);
        log.warn("Suscripción {} cancelada.", id);
        return suscripcionRepository.save(suscripcion);
    }

    @Override
    public Suscripcion actualizarEstado(Long id, EstadoSuscripcion nuevoEstado) {
        Suscripcion suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada con ID: " + id));

        suscripcion.setEstado(nuevoEstado);
        log.info("Suscripción {} actualizada a estado {}", id, nuevoEstado);
        return suscripcionRepository.save(suscripcion);
    }

    @Override
    public List<Suscripcion> listarPorUsuario(Long usuarioId) {
        return suscripcionRepository.findByUsuarioId(usuarioId)
                .stream()
                .sorted((a, b) -> b.getFechaInicio().compareTo(a.getFechaInicio()))
                .toList();
    }


}
