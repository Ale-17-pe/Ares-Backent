package com.micro.reservasservices.Repository;

import com.micro.reservasservices.Model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    long countByClaseId(Long claseId);
    List<Reserva> findByClaseId(Long claseId);
    List<Reserva> findByUsuarioId(Long usuarioId);
}
