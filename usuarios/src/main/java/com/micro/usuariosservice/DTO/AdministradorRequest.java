package com.micro.usuariosservice.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdministradorRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe proporcionar un correo válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos numéricos")
    private String dni;

    private String direccion;

    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos numéricos")
    private String telefono;

    @NotBlank(message = "El nivel de acceso es obligatorio")
    private String nivelAcceso;

    @NotBlank(message = "El departamento es obligatorio")
    private String departamento;
}
