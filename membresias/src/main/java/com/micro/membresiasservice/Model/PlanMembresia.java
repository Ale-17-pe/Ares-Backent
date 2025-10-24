package com.micro.membresiasservice.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "planes_membresia")
public class PlanMembresia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombrePlan;
    private String descripcion;
    private Double precio;
    private Integer duracionEnDias;
}
