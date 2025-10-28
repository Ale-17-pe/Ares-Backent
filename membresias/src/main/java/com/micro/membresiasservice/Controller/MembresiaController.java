package com.micro.membresiasservice.Controller;

import com.micro.membresiasservice.DTO.ApiResponse;
import com.micro.membresiasservice.DTO.SuscripcionRequest;
import com.micro.membresiasservice.Model.Enum.EstadoSuscripcion;
import com.micro.membresiasservice.Model.PlanMembresia;
import com.micro.membresiasservice.Model.Suscripcion;
import com.micro.membresiasservice.Services.MembresiaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/membresias")
public class MembresiaController {

    private final MembresiaService membresiaService;

    public MembresiaController(MembresiaService membresiaService) {
        this.membresiaService = membresiaService;
    }

    // ===========================
    // 🔹 PLANES
    // ===========================

    @PostMapping("/planes")
    public ResponseEntity<ApiResponse<PlanMembresia>> crearPlan(@RequestBody PlanMembresia planMembresia) {
        PlanMembresia nuevo = membresiaService.crearPlan(planMembresia);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Plan creado exitosamente", nuevo));
    }

    @GetMapping("/planes")
    public ResponseEntity<ApiResponse<List<PlanMembresia>>> listarPlanes() {
        return ResponseEntity.ok(ApiResponse.success("Listado de planes", membresiaService.listarPlanes()));
    }

    @GetMapping("/planes/{id}")
    public ResponseEntity<ApiResponse<PlanMembresia>> obtenerPlan(@PathVariable Long id) {
        return membresiaService.obtenerPlan(id)
                .map(plan -> ResponseEntity.ok(ApiResponse.success("Plan encontrado", plan)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Plan no encontrado con ID: " + id)));
    }

    @PutMapping("/planes/{id}")
    public ResponseEntity<ApiResponse<PlanMembresia>> actualizarPlan(@PathVariable Long id,
                                                                     @RequestBody PlanMembresia planActualizado) {
        try {
            PlanMembresia actualizado = membresiaService.actualizarPlan(id, planActualizado);
            return ResponseEntity.ok(ApiResponse.success("Plan actualizado correctamente", actualizado));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }
    @DeleteMapping("/planes/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarPlan(@PathVariable Long id) {
        try {
            membresiaService.eliminarPlan(id);
            return ResponseEntity.ok(ApiResponse.success("Plan eliminado exitosamente", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    // ===========================
    // 🔹 SUSCRIPCIONES
    // ===========================

    @PostMapping("/suscripciones")
    public ResponseEntity<ApiResponse<Suscripcion>> crearSuscripcion(@RequestBody SuscripcionRequest request) {
        try {
            Suscripcion nueva = membresiaService.crearSuscripcion(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Suscripción creada exitosamente", nueva));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error al crear suscripción: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno al crear la suscripción"));
        }
    }

    @GetMapping("/suscripciones")
    public ResponseEntity<ApiResponse<List<Suscripcion>>> listarSuscripciones() {
        List<Suscripcion> lista = membresiaService.listarTodasLasSuscripciones();
        return ResponseEntity.ok(ApiResponse.success("Listado de suscripciones", lista));
    }

    @GetMapping("/suscripciones/{id}")
    public ResponseEntity<ApiResponse<Suscripcion>> obtenerSuscripcion(@PathVariable Long id) {
        return membresiaService.obtenerSuscripcion(id)
                .map(s -> ResponseEntity.ok(ApiResponse.success("Suscripción encontrada", s)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Suscripción no encontrada con ID: " + id)));
    }

    @GetMapping("/suscripciones/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<Suscripcion>> verificarSuscripcionActiva(@PathVariable Long usuarioId) {
        return membresiaService.verificarSuscripcionActiva(usuarioId)
                .map(s -> ResponseEntity.ok(ApiResponse.success("Suscripción activa encontrada", s)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("El usuario no tiene suscripción activa.")));
    }

    @PutMapping("/suscripciones/{id}/cancelar")
    public ResponseEntity<ApiResponse<Suscripcion>> cancelarSuscripcion(@PathVariable Long id) {
        try {
            Suscripcion cancelada = membresiaService.cancelarSuscripcion(id);
            return ResponseEntity.ok(ApiResponse.success("Suscripción cancelada correctamente", cancelada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/suscripciones/{id}/estado")
    public ResponseEntity<ApiResponse<Suscripcion>> actualizarEstado(@PathVariable Long id,
                                                                     @RequestParam EstadoSuscripcion estado) {
        try {
            Suscripcion actualizada = membresiaService.actualizarEstado(id, estado);
            return ResponseEntity.ok(ApiResponse.success("Estado de suscripción actualizado", actualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }
    @GetMapping("/usuario/{usuarioId}/activa")
    public ResponseEntity<ApiResponse<Boolean>> tieneMembresiaActiva(@PathVariable Long usuarioId) {
        boolean activa = membresiaService.verificarSuscripcionActiva(usuarioId)
                .map(s -> s.getEstado() == EstadoSuscripcion.ACTIVA &&
                        s.getFechaFin().isAfter(LocalDate.now()))
                .orElse(false);

        String mensaje = activa
                ? "El usuario tiene una membresía activa"
                : "El usuario no tiene una membresía activa";
        return ResponseEntity.ok(ApiResponse.success(mensaje, activa));
    }
}
