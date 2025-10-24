package com.micro.membresiasservice.Controller;

import com.micro.membresiasservice.DTO.SuscripcionRequest;
import com.micro.membresiasservice.Model.PlanMembresia;
import com.micro.membresiasservice.Model.Suscripcion;
import com.micro.membresiasservice.Repository.PlanRepository;
import com.micro.membresiasservice.Repository.SuscripcionRepository;
import com.micro.membresiasservice.Services.MembresiaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/membresias")
public class MembresiaController {

    private final MembresiaService membresiaService;

    public MembresiaController(PlanRepository planRepository, MembresiaService membresiaService, SuscripcionRepository suscripcionRepository) {
        this.membresiaService = membresiaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearPlan(@RequestBody PlanMembresia planMembresia) {
        PlanMembresia nueva = membresiaService.crearPlan(planMembresia);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PlanMembresia>> listarPlanes() {
        return ResponseEntity.ok(membresiaService.listarPlanes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanMembresia> obtenerPlan(@PathVariable Long id) {
        return membresiaService.obtenerPlan(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarPlan(@PathVariable Long id, @RequestBody PlanMembresia planActualizado) {
        try {
            PlanMembresia plan = membresiaService.actualizarPlan(id, planActualizado);
            return ResponseEntity.ok(plan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarMembresia(@PathVariable Long id) {
        membresiaService.eliminarPlan(id);
        return ResponseEntity.ok(Map.of("message", "Membresía eliminada"));
    }

    @PostMapping("/suscripciones")
    public ResponseEntity<?> crearSuscripcion(@RequestBody SuscripcionRequest request) {
        try {
            Suscripcion nuevaSuscripcion = membresiaService.crearSuscripcion(request);
            return new ResponseEntity<>(nuevaSuscripcion, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/suscripciones/usuario/{usuarioId}")
    public ResponseEntity<?> verificarSuscripcionActiva(@PathVariable Long usuarioId) {
        return membresiaService.verificarSuscripcionActiva(usuarioId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No hay suscripción activa")));
    }

    @GetMapping("/suscripciones")
    public ResponseEntity<List<Suscripcion>> listarTodasLasSuscripciones() {
        return ResponseEntity.ok(membresiaService.listarTodasLasSuscripciones());
    }

    @GetMapping("/usuario/{usuarioId}/activa")
    public ResponseEntity<Boolean> tieneMembresiaActiva(@PathVariable Long usuarioId) {
        try {
            boolean activa = membresiaService.verificarSuscripcionActiva(usuarioId)
                    .map(suscripcion -> {
                        // Verifica que esté activa y no vencida
                        return suscripcion.getEstado().equalsIgnoreCase("ACTIVA")
                                && suscripcion.getFechaFin().isAfter(LocalDate.now());
                    })
                    .orElse(false);

            return ResponseEntity.ok(activa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
