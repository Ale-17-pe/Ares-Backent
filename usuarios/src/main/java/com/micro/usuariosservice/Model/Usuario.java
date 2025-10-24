package com.micro.usuariosservice.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String role;
    private String dni;

    private String direccion;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String genero;

    private String turno;
    private BigDecimal sueldo;
    private String sedeAsignada;

    private Boolean activo = true;
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}