package com.micro.asistenciasservice.Controller;

import com.micro.asistenciasservice.Model.Asistencia;
import com.micro.asistenciasservice.Repository.AsistenciaRepository;
import com.micro.asistenciasservice.Services.AsistenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    private final AsistenciaService service;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.service = asistenciaService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<Asistencia> registrarAsistencia(
            @RequestParam Long idUsuario,
            @RequestParam(required = false) Long idReserva)
    {
        Asistencia resultado = service.registrarEntrada(idUsuario, idReserva);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Asistencia>> obtenerHistorialPorUsuario(@PathVariable Long idUsuario) {
        List<Asistencia> historial = service.listarAsistenciaClientes(idUsuario);
        return ResponseEntity.ok(historial);
    }
}
