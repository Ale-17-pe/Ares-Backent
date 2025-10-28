package com.micro.usuariosservice.Repository;

import com.micro.usuariosservice.Model.Administrador;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministradorRepository extends UsuarioRepositoryBase<Administrador> {
    // 🔹 Buscar administradores por departamento
    @Query("SELECT a FROM Administrador a WHERE LOWER(a.departamento) = LOWER(:departamento) AND a.activo = true")
    List<Administrador> findByDepartamento(String departamento);

    // 🔹 Buscar administradores por nivel de acceso
    @Query("SELECT a FROM Administrador a WHERE LOWER(a.nivelAcceso) = LOWER(:nivelAcceso) AND a.activo = true")
    List<Administrador> findByNivelAcceso(String nivelAcceso);
}
