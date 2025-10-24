package com.micro.asistenciasservice.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "asistencias")
@Data
@NoArgsConstructor
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUsuario;
    private Long idReserva;
    private Instant fechaHoraEntrada = Instant.now();

    @Enumerated(EnumType.STRING)
    private EstadoAsistencia estado = EstadoAsistencia.FALLIDA;
    private String motivoFallo;
    private Instant fechaHoraSalida;

}