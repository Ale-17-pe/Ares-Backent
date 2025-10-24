package com.micro.calificacionesservice.Service.Impl;

import com.micro.calificacionesservice.Model.EstadoOpinion;
import com.micro.calificacionesservice.Model.Opinion;
import com.micro.calificacionesservice.Repository.OpinionRepository;
import com.micro.calificacionesservice.Service.CalificacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalDouble;

@Service
@Transactional
public class CalificacionServiceImpl implements CalificacionService {

    private final OpinionRepository opinionRepository;

    public CalificacionServiceImpl(OpinionRepository opinionRepository) {
        this.opinionRepository = opinionRepository;
    }

    @Override
    public Opinion crearOpinion(Opinion nuevaOpinion) {
        if (nuevaOpinion.getPuntuacion() < 1 || nuevaOpinion.getPuntuacion() > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5.");
        }
        nuevaOpinion.setEstado(EstadoOpinion.PENDIENTE);
        return opinionRepository.save(nuevaOpinion);
    }

    @Override
    public List<Opinion> obtenerOpinionesAprobadas(String tipoEntidad, Long idEntidad) {
        return opinionRepository.findByTipoEntidadAndIdEntidadAndEstado(
                tipoEntidad, idEntidad, EstadoOpinion.APROBADA);
    }

    @Override
    public double calcularPromedio(String tipoEntidad, Long idEntidad) {
        List<Opinion> opiniones = obtenerOpinionesAprobadas(tipoEntidad, idEntidad);

        if (opiniones.isEmpty()) {
            return 0.0;
        }
        OptionalDouble promedio = opiniones.stream()
                .mapToInt(Opinion::getPuntuacion)
                .average();
        return Math.round(promedio.orElse(0.0) * 100.0) / 100.0;
    }
}
