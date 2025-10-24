package com.micro.usuariosservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private String nombre;
    private String email;
    private String role;
    private String dni;
    private String direccion;
    private String telefono;
}
