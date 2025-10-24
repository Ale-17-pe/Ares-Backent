package com.micro.usuariosservice.DTO;

import com.micro.usuariosservice.Model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {
    private String message;
    private Usuario usuario;
}
