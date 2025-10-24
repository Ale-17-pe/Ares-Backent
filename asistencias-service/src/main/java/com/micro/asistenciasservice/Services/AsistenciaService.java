package com.micro.asistenciasservice.Services;

import com.micro.asistenciasservice.Model.Asistencia;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

public interface AsistenciaService {
    Asistencia registrarEntrada(Long idUsuario, Long idReserva);
    boolean validarMembresia(Long idUsuario);
    boolean validarReserva(Long idUsuario, Long idReserva);
    List<Asistencia> listarAsistenciaClientes(Long idUsuario);
    Optional<Asistencia> buscarPorId(Long id);
}
