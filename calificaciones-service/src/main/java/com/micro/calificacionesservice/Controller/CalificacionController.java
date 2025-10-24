package com.micro.calificacionesservice.Controller;

import com.micro.calificacionesservice.Model.Opinion;
import com.micro.calificacionesservice.Service.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {


    private final CalificacionService service;

    public CalificacionController(CalificacionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Opinion> crearOpinion(@RequestBody Opinion opinion) {
        try {
            Opinion nuevaOpinion = service.crearOpinion(opinion);
            // 201 Created: La opinión se ha recibido y está PENDIENTE de moderación
            return new ResponseEntity<>(nuevaOpinion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{tipoEntidad}/{idEntidad}")
    public ResponseEntity<List<Opinion>> obtenerOpiniones(
            @PathVariable String tipoEntidad,
            @PathVariable Long idEntidad) {
        List<Opinion> opiniones = service.obtenerOpinionesAprobadas(tipoEntidad, idEntidad);
        return ResponseEntity.ok(opiniones);
    }

    @GetMapping("/promedio/{tipoEntidad}/{idEntidad}")
    public ResponseEntity<Double> obtenerPromedio(
            @PathVariable String tipoEntidad,
            @PathVariable Long idEntidad) {
        double promedio = service.calcularPromedio(tipoEntidad, idEntidad);
        return ResponseEntity.ok(promedio);
    }
}