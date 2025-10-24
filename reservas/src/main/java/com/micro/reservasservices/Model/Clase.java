package com.micro.reservasservices.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "clases")
public class Clase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreClase;
    private Long instructorId;
    private Integer capacidadMaxima;
}
