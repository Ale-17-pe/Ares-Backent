package com.micro.asistenciasservice.Services.Impl;

import com.micro.asistenciasservice.Model.Asistencia;
import com.micro.asistenciasservice.Model.EstadoAsistencia;
import com.micro.asistenciasservice.Repository.AsistenciaRepository;
import com.micro.asistenciasservice.Services.AsistenciaService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final WebClient webClient;

    public AsistenciaServiceImpl(AsistenciaRepository asistenciaRepository, WebClient.Builder webClientBuilder) {
        this.asistenciaRepository = asistenciaRepository;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/").build();
    }

    @Override
    public Asistencia registrarEntrada(Long idUsuario, Long idReserva) {
        Asistencia nuevaAsistencia = new Asistencia();
        nuevaAsistencia.setIdUsuario(idUsuario);
        nuevaAsistencia.setIdReserva(idReserva);

        String motivoFallo = null;

        if (!validarMembresia(idUsuario)) {
            motivoFallo = "Membresía Inactiva o Vencida";
        }

        if (idReserva != null && motivoFallo == null) {
            if (!validarReserva(idUsuario, idReserva)) {
                motivoFallo = "Reserva No Encontrada o Fuera de Horario";
            }
        }
        if (motivoFallo == null) {
            nuevaAsistencia.setEstado(EstadoAsistencia.VALIDA);
            System.out.println("✅ ASISTENCIA VÁLIDA: Publicando AsistenciaRegistradaEvent...");
        } else {
            nuevaAsistencia.setMotivoFallo(motivoFallo);
            System.err.println("❌ ASISTENCIA FALLIDA: " + motivoFallo);
        }

        return asistenciaRepository.save(nuevaAsistencia);
    }

    @Override
    public boolean validarMembresia(Long idUsuario) {
        try {
            Boolean activa = webClient.get()
                    .uri("/api/membresias/usuario/{idUsuario}/activa", idUsuario)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return activa != null && activa;
        } catch (Exception e) {
            System.err.println("Error al contactar a membresias-service: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean validarReserva(Long idUsuario, Long idReserva) {
        try {
            Boolean valida = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/reservas/{idReserva}/validar")
                            .queryParam("idUsuario", idUsuario)
                            .build(idReserva))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return valida != null && valida;
        } catch (Exception e) {
            System.err.println("Error al contactar a reservas-service: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Asistencia> listarAsistenciaClientes(Long idUsuario) {
        return asistenciaRepository.findByIdUsuario(idUsuario);
    }

    @Override
    public Optional<Asistencia> buscarPorId(Long id) {
        return asistenciaRepository.findById(id);
    }
}
