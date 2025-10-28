package com.micro.usuariosservice.Model;

import com.micro.usuariosservice.Model.Enum.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@PrimaryKeyJoinColumn(name = "recepcionista_id")
@Table(name = "recepcionistas")
public class Recepcionista extends Usuario {

    @NotBlank(message = "El código de empleado es obligatorio")
    private String codigoEmpleado;

    private LocalDate fechaContratacion;

    @NotBlank(message = "El turno asignado es obligatorio")
    private String turnoAsignado;

    public Recepcionista() {
        this.setRole(Roles.RECEPCIONISTA);
    }
}
