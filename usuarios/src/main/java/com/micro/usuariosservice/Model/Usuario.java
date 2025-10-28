package com.micro.usuariosservice.Model;

import com.micro.usuariosservice.Model.Enum.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "usuarios",
        indexes = {
                @Index(name = "idx_usuario_email", columnList = "email"),
                @Index(name = "idx_usuario_dni", columnList = "dni")
        }
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    private String apellido;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Column(unique = true)
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos numéricos")
    private String dni;

    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    private String direccion;

    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos numéricos")
    private String telefono;

    private Boolean activo = true;

    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", updatable = false, nullable = false)
    private Roles role;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}
