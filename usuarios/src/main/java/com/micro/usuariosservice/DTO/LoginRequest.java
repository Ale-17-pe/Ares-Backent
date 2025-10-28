package com.micro.usuariosservice.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Debe ingresar su correo o DNI")
    private String identificador;

    @NotBlank(message = "Debe ingresar su contraseña")
    private String password;
}
