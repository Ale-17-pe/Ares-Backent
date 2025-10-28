package com.micro.usuariosservice.Repository;

import com.micro.usuariosservice.Model.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends UsuarioRepositoryBase<Cliente>{
    @Query("SELECT c FROM Cliente c WHERE c.aceptaPromociones = true AND c.activo = true")
    List<Cliente> findClientesQueAceptanPromociones();

    // 🔹 Buscar clientes por sexo
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.sexo) = LOWER(:sexo)")
    List<Cliente> findBySexo(String sexo);

}