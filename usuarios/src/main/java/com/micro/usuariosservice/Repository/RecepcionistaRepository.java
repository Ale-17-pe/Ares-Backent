package com.micro.usuariosservice.Repository;

import com.micro.usuariosservice.Model.Recepcionista;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecepcionistaRepository extends UsuarioRepositoryBase<Recepcionista> {
    @Query("SELECT r FROM Recepcionista r WHERE LOWER(r.turnoAsignado) = LOWER(:turno) AND r.activo = true")
    List<Recepcionista> findByTurnoAsignado(String turno);

    // 🔹 Buscar recepcionistas contratados en una fecha específica
    @Query("SELECT r FROM Recepcionista r WHERE r.fechaContratacion = :fecha")
    List<Recepcionista> findByFechaContratacion(java.time.LocalDate fecha);
}
