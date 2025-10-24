package com.micro.calificacionesservice.Repository;

import com.micro.calificacionesservice.Model.EstadoOpinion;
import com.micro.calificacionesservice.Model.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpinionRepository extends JpaRepository<Opinion, Integer> {
    List<Opinion> findByTipoEntidadAndIdEntidadAndEstado(
            String tipoEntidad, Long idEntidad, EstadoOpinion estado
    );
}
