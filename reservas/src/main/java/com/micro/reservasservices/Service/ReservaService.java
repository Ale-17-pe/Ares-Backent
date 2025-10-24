package com.micro.reservasservices.Service;

import com.micro.reservasservices.DTO.ReservaRequest;
import com.micro.reservasservices.Model.Clase;
import com.micro.reservasservices.Model.Reserva;

import java.util.List;

public interface ReservaService {
    Clase crearClase(Clase clase);
    List<Clase> listarClases();
    Clase actualizarClase(Long id, Clase claseActualizada);
    void eliminarClase(Long id);
    Reserva crearReserva(ReservaRequest request);
    List<Reserva> obtenerReservasPorUsuario(Long usuarioId);
}
