package com.micro.usuariosservice.Controller;

import com.micro.usuariosservice.Model.Usuario;
import com.micro.usuariosservice.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrar(usuario);
            nuevoUsuario.setPassword(null);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente");
            response.put("usuario", nuevoUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar usuario"));
        }
    }
    @PostMapping("/crear-recepcionista")
    public ResponseEntity<?> crearRecepcionista(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrarRecepcionista(usuario);
            nuevo.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Recepcionista creado exitosamente",
                    "usuario", nuevo
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Usuario usuario) {
        return usuarioService.login(usuario)
                .<ResponseEntity<?>>map(user -> {
                    user.setPassword(null);

                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Inicio de sesión exitoso");
                    response.put("usuario", user);

                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Credenciales inválidas")));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        try {
            Usuario actualizado = usuarioService.actualizar(id, datos);
            actualizado.setPassword(null);
            return ResponseEntity.ok(Map.of(
                    "message", "Perfil actualizado correctamente",
                    "usuario", actualizado
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listar();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(id);
            if (usuario != null) {
                usuario.setPassword(null);
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener usuario"));
        }
    }
}
