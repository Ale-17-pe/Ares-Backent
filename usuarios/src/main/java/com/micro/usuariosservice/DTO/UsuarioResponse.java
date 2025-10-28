package com.micro.usuariosservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micro.usuariosservice.Model.Enum.Roles;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Roles role;

    private String dni;
    private String direccion;
    private String telefono;

    private Boolean activo;
    private LocalDateTime fechaRegistro;
}
