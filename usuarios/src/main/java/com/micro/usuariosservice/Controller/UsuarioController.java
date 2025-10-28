package com.micro.usuariosservice.Controller;

import com.micro.usuariosservice.DTO.*;
import com.micro.usuariosservice.Model.Usuario;
import com.micro.usuariosservice.Service.UsuarioService;
import com.micro.usuariosservice.Util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public UsuarioController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    // 🟩 Registrar Cliente
    @PostMapping("/clientes")
    public ResponseEntity<ApiResponse<Long>> registrarCliente(@Valid @RequestBody ClienteRequest request) {
        Usuario nuevoCliente = usuarioService.registrarCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cliente registrado exitosamente", nuevoCliente.getId()));
    }

    // 🟩 Registrar Recepcionista
    @PostMapping("/recepcionistas")
    public ResponseEntity<ApiResponse<Long>> crearRecepcionista(@Valid @RequestBody RecepcionistaRequest request) {
        Usuario nuevoRecepcionista = usuarioService.registrarRecepcionista(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Recepcionista creado exitosamente", nuevoRecepcionista.getId()));
    }

    // 🟩 Registrar Administrador
    @PostMapping("/administradores")
    public ResponseEntity<ApiResponse<Long>> crearAdministrador(@Valid @RequestBody AdministradorRequest request) {
        Usuario nuevoAdministrador = usuarioService.registrarAdministrador(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Administrador creado exitosamente", nuevoAdministrador.getId()));
    }

    // 🟨 Login de usuario
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUsuario(@Valid @RequestBody LoginRequest credenciales) {
        return usuarioService.login(credenciales)
                .map(user -> {
                    String token = jwtUtil.generateToken(user);
                    LoginResponse response = usuarioService.mapToLoginResponse(user);
                    response.setToken(token);
                    return ResponseEntity.ok(ApiResponse.success("Inicio de sesión exitoso", response));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Credenciales inválidas")));
    }

    // 🟦 Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setPassword(null);
        return ResponseEntity.ok(ApiResponse.success("Usuario encontrado", usuario));
    }

    // 🟩 Actualizar datos de usuario
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ApiResponse<Usuario>> actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        Usuario actualizado = usuarioService.actualizar(id, datos);
        actualizado.setPassword(null);
        return ResponseEntity.ok(ApiResponse.success("Perfil actualizado correctamente", actualizado));
    }

    // 🟪 Listar administradores
    @GetMapping("/administradores")
    public ResponseEntity<ApiResponse<List<? extends Usuario>>> listarAdministradores() {
        List<? extends Usuario> administradores = usuarioService.listarSoloAdministradores();
        administradores.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(ApiResponse.success("Lista de administradores obtenida correctamente", administradores));
    }

    // 🟦 Listar todos los usuarios
    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        usuarios.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(ApiResponse.success("Lista de usuarios obtenida correctamente", usuarios));
    }

    // 🟥 Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario eliminado exitosamente", null));
    }
}
