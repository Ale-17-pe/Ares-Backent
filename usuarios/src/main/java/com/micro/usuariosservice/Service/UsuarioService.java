package com.micro.usuariosservice.Service;

import com.micro.usuariosservice.Model.Usuario;
import com.micro.usuariosservice.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface UsuarioService {
    Usuario registrar(Usuario usuario);
    Usuario registrarRecepcionista(Usuario usuario);
    Optional<Usuario> login(Usuario usuario);
    Usuario actualizar(Long id, Usuario datosActualizados);
    List<Usuario> listar();
    void eliminar(Long id);
}
