package com.micro.usuariosservice.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteResponse extends UsuarioResponse {
    private String sexo;
    private LocalDate fechaNacimiento;
    private Boolean aceptaPromociones;
}
