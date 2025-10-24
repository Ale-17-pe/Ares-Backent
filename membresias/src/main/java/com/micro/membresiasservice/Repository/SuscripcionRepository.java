package com.micro.membresiasservice.Repository;

import com.micro.membresiasservice.Model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    Optional<Suscripcion> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
