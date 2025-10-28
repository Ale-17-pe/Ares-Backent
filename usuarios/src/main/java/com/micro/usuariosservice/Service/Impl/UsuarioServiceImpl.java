package com.micro.usuariosservice.Service.Impl;

import com.micro.usuariosservice.DTO.*;
import com.micro.usuariosservice.Model.Administrador;
import com.micro.usuariosservice.Model.Cliente;
import com.micro.usuariosservice.Model.Recepcionista;
import com.micro.usuariosservice.Model.Usuario;
import com.micro.usuariosservice.Repository.AdministradorRepository;
import com.micro.usuariosservice.Repository.ClienteRepository;
import com.micro.usuariosservice.Repository.RecepcionistaRepository;
import com.micro.usuariosservice.Service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final ClienteRepository clienteRepository;
    private final RecepcionistaRepository recepcionistaRepository;
    private final AdministradorRepository administradorRepository;

    public UsuarioServiceImpl(ClienteRepository clienteRepository,
                              RecepcionistaRepository recepcionistaRepository,
                              AdministradorRepository administradorRepository) {
        this.clienteRepository = clienteRepository;
        this.recepcionistaRepository = recepcionistaRepository;
        this.administradorRepository = administradorRepository;
    }

    private void validarUnicidad(String email, String dni) {
        if (clienteRepository.existsByEmail(email) || recepcionistaRepository.existsByEmail(email) || administradorRepository.existsByEmail(email)) {
            throw new IllegalStateException("El email ya está registrado");
        }
        if (dni != null && (clienteRepository.existsByDni(dni) || recepcionistaRepository.existsByDni(dni) || administradorRepository.existsByDni(dni))) {
            throw new IllegalStateException("El DNI ya está registrado");
        }
    }

    public LoginResponse mapToLoginResponse(Usuario usuario) {
        LoginResponse response = new LoginResponse();

        // Campos comunes
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setEmail(usuario.getEmail());
        response.setRole(usuario.getRole());
        response.setDni(usuario.getDni());
        response.setDireccion(usuario.getDireccion());
        response.setTelefono(usuario.getTelefono());

        return response;
    }

    @Override
    public Cliente registrarCliente(ClienteRequest request) {
        validarUnicidad(request.getEmail(), request.getDni());

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setEmail(request.getEmail());
        cliente.setDni(request.getDni());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setSexo(request.getSexo());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setAceptaPromociones(request.getAceptaPromociones());

        String hashed = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        cliente.setPassword(hashed);

        return clienteRepository.save(cliente);
    }

    @Override
    public Recepcionista registrarRecepcionista(RecepcionistaRequest request) {
        validarUnicidad(request.getEmail(), request.getDni());

        // Mapeo: RecepcionistaRequest -> Entidad Recepcionista
        Recepcionista recepcionista = new Recepcionista();
        recepcionista.setNombre(request.getNombre());
        recepcionista.setApellido(request.getApellido());
        recepcionista.setEmail(request.getEmail());
        recepcionista.setDni(request.getDni());
        recepcionista.setDireccion(request.getDireccion());
        recepcionista.setTelefono(request.getTelefono());
        recepcionista.setCodigoEmpleado(request.getCodigoEmpleado());
        recepcionista.setFechaContratacion(request.getFechaContratacion());
        recepcionista.setTurnoAsignado(request.getTurnoAsignado());

        // Contraseña
        String hashed = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        recepcionista.setPassword(hashed);

        return recepcionistaRepository.save(recepcionista);
    }

    @Override
    public Administrador registrarAdministrador(AdministradorRequest request) {
        validarUnicidad(request.getEmail(), request.getDni());

        // Mapeo: AdministradorRequest -> Entidad Administrador
        Administrador administrador = new Administrador();
        administrador.setNombre(request.getNombre());
        administrador.setApellido(request.getApellido());
        administrador.setEmail(request.getEmail());
        administrador.setDni(request.getDni());
        administrador.setDireccion(request.getDireccion());
        administrador.setTelefono(request.getTelefono());
        administrador.setNivelAcceso(request.getNivelAcceso());
        administrador.setDepartamento(request.getDepartamento());

        // Contraseña
        String hashed = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        administrador.setPassword(hashed);

        return administradorRepository.save(administrador);
    }

    @Override
    public Optional<Usuario> login(LoginRequest credenciales) {
        String identificador = credenciales.getIdentificador();
        String password = credenciales.getPassword();

        Optional<Usuario> usuarioEncontrado;

        // 1. INTENTO DE BÚSQUEDA Y VERIFICACIÓN POR EMAIL
        usuarioEncontrado = buscarYVerificarPorIdentificador(identificador, password, true);

        // 2. SI NO SE ENCUENTRA POR EMAIL, INTENTO DE BÚSQUEDA Y VERIFICACIÓN POR DNI
        if (usuarioEncontrado.isEmpty()) {
            usuarioEncontrado = buscarYVerificarPorIdentificador(identificador, password, false);
        }

        return usuarioEncontrado;
    }

    private Optional<Usuario> buscarYVerificarPorIdentificador(
            String identificador, String password, boolean buscarPorEmail) {

        Optional<? extends Usuario> usuarioOpt = Optional.empty();

        if (buscarPorEmail) {
            usuarioOpt = clienteRepository.findByEmail(identificador);
            if (usuarioOpt.isEmpty()) {
                usuarioOpt = recepcionistaRepository.findByEmail(identificador);
            }
            if (usuarioOpt.isEmpty()) {
                usuarioOpt = administradorRepository.findByEmail(identificador);
            }
        } else {
            usuarioOpt = clienteRepository.findByDni(identificador);
            if (usuarioOpt.isEmpty()) {
                usuarioOpt = recepcionistaRepository.findByDni(identificador);
            }
            if (usuarioOpt.isEmpty()) {
                usuarioOpt = administradorRepository.findByDni(identificador);
            }
        }

        if (usuarioOpt.isPresent()) {
            Usuario user = usuarioOpt.get();
            if (BCrypt.checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Usuario actualizar(Long id, Usuario datosActualizados) {
        Usuario usuarioExistente = obtenerPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        usuarioExistente.setNombre(datosActualizados.getNombre());
        usuarioExistente.setApellido(datosActualizados.getApellido());
        usuarioExistente.setTelefono(datosActualizados.getTelefono());
        usuarioExistente.setDireccion(datosActualizados.getDireccion());

        if (usuarioExistente instanceof Cliente) {
            Cliente clienteExistente = (Cliente) usuarioExistente;
            Cliente clienteActualizado = (Cliente) datosActualizados;

            clienteExistente.setSexo(clienteActualizado.getSexo());
            clienteExistente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
            clienteExistente.setAceptaPromociones(clienteActualizado.getAceptaPromociones());

            return clienteRepository.save(clienteExistente);

        } else if (usuarioExistente instanceof Recepcionista) {
            Recepcionista recepExistente = (Recepcionista) usuarioExistente;
            Recepcionista recepActualizada = (Recepcionista) datosActualizados;

            recepExistente.setCodigoEmpleado(recepActualizada.getCodigoEmpleado());
            recepExistente.setTurnoAsignado(recepActualizada.getTurnoAsignado());

            return recepcionistaRepository.save(recepExistente);

        } else if (usuarioExistente instanceof Administrador) {
            Administrador adminExistente = (Administrador) usuarioExistente;
            Administrador adminActualizado = (Administrador) datosActualizados;

            adminExistente.setNivelAcceso(adminActualizado.getNivelAcceso());
            adminExistente.setDepartamento(adminActualizado.getDepartamento());

            return administradorRepository.save(adminExistente);
        } else {
            throw new IllegalStateException("Tipo de usuario desconocido para actualizar");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(clienteRepository.findAll());
        todos.addAll(recepcionistaRepository.findAll());
        todos.addAll(administradorRepository.findAll());
        return todos;
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) return Optional.of(clienteOpt.get());

        Optional<Recepcionista> recepOpt = recepcionistaRepository.findById(id);
        if (recepOpt.isPresent()) return Optional.of(recepOpt.get());

        Optional<Administrador> adminOpt = administradorRepository.findById(id);
        if (adminOpt.isPresent()) return Optional.of(adminOpt.get());

        return Optional.empty();
    }

    @Override
    public void eliminar(Long id) {
        Optional<Usuario> usuarioOpt = obtenerPorId(id);
        if (usuarioOpt.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }

        Usuario usuario = usuarioOpt.get();
        if (usuario instanceof Cliente) {
            clienteRepository.deleteById(id);
        } else if (usuario instanceof Recepcionista) {
            recepcionistaRepository.deleteById(id);
        } else if (usuario instanceof Administrador) {
            administradorRepository.deleteById(id);
        }
    }


    @Override
    public Optional<Usuario> obtenerPorEmail(String email) {
        Optional<? extends Usuario> usuarioOpt = Optional.empty();

        // 1. Cliente
        usuarioOpt = clienteRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) return (Optional<Usuario>) usuarioOpt;

        // 2. Recepcionista
        usuarioOpt = recepcionistaRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) return (Optional<Usuario>) usuarioOpt;

        // 3. Administrador
        usuarioOpt = administradorRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) return (Optional<Usuario>) usuarioOpt;

        return Optional.empty();
    }

    @Override
    public List<Administrador> listarSoloAdministradores() {
        return administradorRepository.findAll();
    }
}