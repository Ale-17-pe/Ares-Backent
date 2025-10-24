package com.micro.reservasservices.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservas")
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long usuarioId;
    @ManyToOne @JoinColumn(name = "clase_id")
    private Clase clase;
    private LocalDateTime fechaHoraReserva;
}
