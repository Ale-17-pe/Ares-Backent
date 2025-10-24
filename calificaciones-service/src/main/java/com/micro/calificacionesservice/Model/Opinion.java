package com.micro.calificacionesservice.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "opiniones")
@Data
@NoArgsConstructor
public class Opinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUsuario;
    private String tipoEntidad;
    private Long idEntidad;
    private Integer puntuacion;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Enumerated(EnumType.STRING)
    private EstadoOpinion estado = EstadoOpinion.PENDIENTE; // Para moderación

    private Instant fechaCreacion = Instant.now();

}
