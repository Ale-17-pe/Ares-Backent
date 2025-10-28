package com.micro.usuariosservice.DTO;

import com.micro.usuariosservice.Model.Enum.Roles;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Roles role;
    private String dni;
    private String direccion;
    private String telefono;
    private String token;
}
