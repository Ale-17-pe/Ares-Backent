package com.micro.reservasservices.Controller;

import com.micro.reservasservices.DTO.ReservaRequest;
import com.micro.reservasservices.DTO.SuscripcionDto;
import com.micro.reservasservices.Model.Clase;
import com.micro.reservasservices.Model.Reserva;
import com.micro.reservasservices.Repository.ClaseRepository;
import com.micro.reservasservices.Repository.ReservaRepository;
import com.micro.reservasservices.Service.ReservaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }


    @PostMapping("/clases")
    public ResponseEntity<Clase> crearClase(@RequestBody Clase clase) {
        return new ResponseEntity<>(reservaService.crearClase(clase), HttpStatus.CREATED);
    }

    @GetMapping("/clases")
    public ResponseEntity<List<Clase>> obtenerClases() {
        return ResponseEntity.ok(reservaService.listarClases());
    }

    @PutMapping("/clases/{id}")
    public ResponseEntity<Clase> actualizarClase(@PathVariable Long id, @RequestBody Clase claseActualizada) {
        try {
            return ResponseEntity.ok(reservaService.actualizarClase(id, claseActualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/clases/{id}")
    public ResponseEntity<Void> eliminarClase(@PathVariable Long id) {
        reservaService.eliminarClase(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody ReservaRequest request) {
        try {
            Reserva nuevaReserva = reservaService.crearReserva(request);
            return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
        } catch (IllegalStateException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reserva>> obtenerReservasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorUsuario(usuarioId));
    }
}

