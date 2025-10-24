    package com.micro.membresiasservice.Model;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDate;

    @Data
    @Entity
    @Table(name = "suscripciones")
    public class Suscripcion {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Long usuarioId;

        @ManyToOne
        @JoinColumn(name = "plan_id")
        private PlanMembresia plan;

        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private String estado;
    }
