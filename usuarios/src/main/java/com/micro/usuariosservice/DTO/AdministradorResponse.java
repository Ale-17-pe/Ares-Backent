package com.micro.usuariosservice.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdministradorResponse extends UsuarioResponse {
    private String nivelAcceso;
    private String departamento;
}
