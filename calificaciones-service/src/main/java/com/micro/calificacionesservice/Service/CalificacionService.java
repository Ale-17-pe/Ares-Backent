package com.micro.calificacionesservice.Service;

import com.micro.calificacionesservice.Model.Opinion;

import java.util.List;

public interface CalificacionService {

    Opinion crearOpinion(Opinion nuevaOpinion);
    List<Opinion> obtenerOpinionesAprobadas(String tipoEntidad, Long idEntidad);
    double calcularPromedio(String tipoEntidad, Long idEntidad);
}
