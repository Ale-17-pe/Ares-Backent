package com.micro.usuariosservice.Service.Impl;

import com.micro.usuariosservice.Model.Usuario;
import com.micro.usuariosservice.Repository.UsuarioRepository;
import com.micro.usuariosservice.Service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalStateException("El email ya está registrado");
        }

        String hashed = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(hashed);
        usuario.setRole("CLIENTE");
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario registrarRecepcionista(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalStateException("El email ya está registrado");
        }

        String hashed = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(hashed);
        usuario.setRole("RECEPCIONISTA");
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> login(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());

        if (existente.isPresent()) {
            Usuario user = existente.get();

            if (BCrypt.checkpw(usuario.getPassword(), user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public Usuario actualizar(Long id, Usuario datosActualizados) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        usuarioExistente.setNombre(datosActualizados.getNombre());
        usuarioExistente.setApellido(datosActualizados.getApellido());
        usuarioExistente.setTelefono(datosActualizados.getTelefono());
        usuarioExistente.setDireccion(datosActualizados.getDireccion());

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
