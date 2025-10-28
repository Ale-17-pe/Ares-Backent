package com.micro.usuariosservice.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecepcionistaResponse extends UsuarioResponse {
    private String codigoEmpleado;
    private LocalDate fechaContratacion;
    private String turnoAsignado;
}
