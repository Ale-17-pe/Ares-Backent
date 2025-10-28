package com.micro.usuariosservice.Model;

import com.micro.usuariosservice.Model.Enum.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@PrimaryKeyJoinColumn(name = "cliente_id")
@Table(name = "clientes")
public class Cliente extends Usuario {

    private String sexo;

    private LocalDate fechaNacimiento;

    private Boolean aceptaPromociones = false;

    public Cliente() {
        this.setRole(Roles.CLIENTE);
    }
}
