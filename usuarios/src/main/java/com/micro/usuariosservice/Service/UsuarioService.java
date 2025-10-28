// src/main/java/com/micro/usuariosservice/Service/UsuarioService.java
package com.micro.usuariosservice.Service;

import com.micro.usuariosservice.DTO.*;
import com.micro.usuariosservice.Model.Administrador;
import com.micro.usuariosservice.Model.Cliente;
import com.micro.usuariosservice.Model.Recepcionista;
import com.micro.usuariosservice.Model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Cliente registrarCliente(ClienteRequest request);
    Recepcionista registrarRecepcionista(RecepcionistaRequest request);
    Administrador registrarAdministrador(AdministradorRequest request);
    Optional<Usuario> login(LoginRequest credenciales);
    LoginResponse mapToLoginResponse(Usuario usuario);
    Usuario actualizar(Long id, Usuario datosActualizados);
    List<Usuario> listarTodos();
    void eliminar(Long id);
    Optional<Usuario> obtenerPorId(Long id);


    // NUEVO MÉTODO REQUERIDO:
    Optional<Usuario> obtenerPorEmail(String email);
    List<Administrador> listarSoloAdministradores();

}