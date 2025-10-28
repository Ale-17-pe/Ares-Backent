package com.micro.usuariosservice.Model;

import com.micro.usuariosservice.Model.Enum.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@PrimaryKeyJoinColumn(name = "administrador_id")
@Table(name = "administradores")
public class Administrador extends Usuario {

    @NotBlank(message = "El nivel de acceso es obligatorio")
    private String nivelAcceso;

    @NotBlank(message = "El departamento es obligatorio")
    private String departamento;

    public Administrador() {
        this.setRole(Roles.ADMIN);
    }
}
