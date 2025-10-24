package com.micro.reservasservices.Service.Impl;

import com.micro.reservasservices.DTO.ReservaRequest;
import com.micro.reservasservices.DTO.SuscripcionDto;
import com.micro.reservasservices.Model.Clase;
import com.micro.reservasservices.Model.Reserva;
import com.micro.reservasservices.Repository.ClaseRepository;
import com.micro.reservasservices.Repository.ReservaRepository;
import com.micro.reservasservices.Service.ReservaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservaServiceImpl implements ReservaService {

    private final RestTemplate restTemplate;
    private final ClaseRepository claseRepository;
    private final ReservaRepository reservaRepository;

    public ReservaServiceImpl(RestTemplate restTemplate, ClaseRepository claseRepository, ReservaRepository reservaRepository) {
        this.restTemplate = restTemplate;
        this.claseRepository = claseRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public Clase crearClase(Clase clase) {
        return claseRepository.save(clase);
    }

    @Override
    public List<Clase> listarClases() {
        return claseRepository.findAll();
    }

    @Override
    public Clase actualizarClase(Long id, Clase claseActualizada) {
        Clase claseExitente = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrado"));
        claseExitente.setNombreClase(claseActualizada.getNombreClase());
        claseExitente.setInstructorId(claseActualizada.getInstructorId());
        claseExitente.setCapacidadMaxima(claseActualizada.getCapacidadMaxima());
        return claseRepository.save(claseExitente);
    }

    @Override
    public void eliminarClase(Long id) {
        if (!claseRepository.existsById(id)) {
            throw new EntityNotFoundException("Clase no encontrada con ID: " + id);
        }
        reservaRepository.deleteAll(reservaRepository.findByClaseId(id));
        claseRepository.deleteById(id);
    }

    @Override
    public Reserva crearReserva(ReservaRequest request) {
        try {
            String url = "http://localhost:8082/api/membresias/suscripciones/usuario/" + request.getUsuarioId();
            ResponseEntity<SuscripcionDto> response = restTemplate.getForEntity(url, SuscripcionDto.class);
            if (response.getStatusCode() != HttpStatus.OK || !"ACTIVA".equals(response.getBody().getEstado())) {
                throw new IllegalStateException("El usuario no tiene una membresía activa.");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalStateException("El usuario no tiene una membresía activa.");
        }

        Clase clase = claseRepository.findById(request.getClaseId())
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));

        long cuposOcupados = reservaRepository.countByClaseId(request.getClaseId());
        if (cuposOcupados >= clase.getCapacidadMaxima()) {
            throw new IllegalStateException("La clase está llena. No hay cupos disponibles.");
        }

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setUsuarioId(request.getUsuarioId());
        nuevaReserva.setClase(clase);
        nuevaReserva.setFechaHoraReserva(LocalDateTime.now());
        return reservaRepository.save(nuevaReserva);
    }

    @Override
    public List<Reserva> obtenerReservasPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }
}
