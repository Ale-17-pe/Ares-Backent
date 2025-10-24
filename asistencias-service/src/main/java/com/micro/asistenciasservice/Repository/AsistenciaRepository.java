package com.micro.asistenciasservice.Repository;

import com.micro.asistenciasservice.Model.Asistencia;
import com.micro.asistenciasservice.Model.EstadoAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByIdUsuarioAndEstado(Long idUsuario, EstadoAsistencia estado);

    List<Asistencia> findByIdUsuario(Long idUsuario);
}
